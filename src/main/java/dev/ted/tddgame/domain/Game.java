package dev.ted.tddgame.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

public class Game extends EventSourcedAggregate {
    private static final int MAXIMUM_NUMBER_OF_PLAYERS = 4;
    private State state;
    private String name;
    private String handle;
    private final Map<MemberId, Player> playerMap = new HashMap<>();
    private final AtomicLong playerIdGenerator = new AtomicLong();
    private final CardsFactory cardsFactory;
    private Deck<ActionCard> actionCardDeck;
    private Deck<TestResultsCard> testResultsCardDeck;
    private final Deck.Shuffler<ActionCard> actionCardShuffler;
    private final Deck.Shuffler<TestResultsCard> testResultsCardShuffler;

    // Rebuilds Game (and its entities) state
    // Public production code should use #reconstitute()
    private Game(List<GameEvent> events,
                 Deck.Shuffler<ActionCard> actionCardShuffler,
                 CardsFactory cardsFactory) {
        this.actionCardShuffler = actionCardShuffler;
        this.cardsFactory = cardsFactory;
        this.testResultsCardShuffler = new Deck.IdentityShuffler<>();

        for (GameEvent event : events) {
            apply(event);
        }
    }

    // Allows control of the shuffler used for the ActionCard deck replenishment
    // And configured CardsFactory to define the cards that end up in the deck
    private Game(Deck.Shuffler<ActionCard> actionCardShuffler,
                 CardsFactory cardsFactory) {
        this.actionCardShuffler = actionCardShuffler;
        this.testResultsCardShuffler = new Deck.IdentityShuffler<>();
        this.cardsFactory = cardsFactory;
    }

    private void initialize(String gameName, String handle) {
        enqueue(new GameCreated(gameName, handle));
        enqueue(new ActionCardDeckCreated(
                        cardsFactory.allActionCards()
                )
        );
        enqueue(new TestResultsCardDeckCreated(
                        cardsFactory.allTestResultsCards()
                )
        );
    }

    @Override
    protected void apply(GameEvent event) {
        switch (event) {
            case GameCreated(String name, String handle) -> {
                this.name = name;
                this.handle = handle;
                this.state = State.WAITING_TO_START;
            }

            case PlayerJoined(MemberId memberId, String playerName) ->
                    playerMap.computeIfAbsent(memberId,
                                              _ -> createPlayer(memberId, playerName));

            case GameStarted _ -> this.state = State.IN_PROGRESS;

            case PlayerEvent playerEvent -> playerFor(playerEvent.memberId())
                    .apply(playerEvent);

            case ActionCardDeckCreated(List<ActionCard> actionCards) ->
                    actionCardDeck = ActionCardDeck.create(
                            actionCards,
                            this::enqueue,
                            actionCardShuffler
                    );

            case TestResultsCardDeckCreated(List<TestResultsCard> testResultsCards) ->
                    testResultsCardDeck = TestResultsCardDeck.create(
                            testResultsCards,
                            this::enqueue,
                            testResultsCardShuffler
                    );

            case ActionCardDeckEvent actionCardDeckEvent ->
                    actionCardDeck.apply(actionCardDeckEvent);

            case TestResultsCardDeckEvent testResultsCardDeckEvent ->
                    testResultsCardDeck.apply(testResultsCardDeckEvent);
        }
    }

    private Player createPlayer(MemberId memberId, String playerName) {
        final PlayerId playerId = new PlayerId(playerIdGenerator.getAndIncrement());
        return new Player(
                playerId,
                memberId,
                playerName,
                this::enqueue,
                new Workspace(playerId));
    }

    public String name() {
        return name;
    }

    public String handle() {
        return handle;
    }

    public List<Player> players() {
        return playerMap.values().stream().toList();
    }

    public void join(MemberId memberId, String playerName) {
        if (!canJoin(memberId)) {
            String ids = playerMap.keySet().stream().map(MemberId::id).toList().toString();
            throw new IllegalStateException("Game is full (Member IDs: " + ids + "), so " + memberId + " cannot join.");
        }

        if (!memberAlreadyInGame(memberId)) {
            enqueue(new PlayerJoined(memberId, playerName));
        }
    }

    public boolean canJoin(MemberId memberId) {
        return gameHasSpaceAvailable() || memberAlreadyInGame(memberId);
    }

    private boolean memberAlreadyInGame(MemberId memberId) {
        return playerMap.containsKey(memberId);
    }

    private boolean gameHasSpaceAvailable() {
        return playerMap.size() < MAXIMUM_NUMBER_OF_PLAYERS;
    }

    public void start() {
        enqueue(new GameStarted());

        initialDealCardsToAllPlayers();
    }

    private void initialDealCardsToAllPlayers() {
        players().forEach(player -> player.drawToFull(actionCardDeck));
    }

    public void discard(MemberId memberId, ActionCard actionCardToDiscard) {
        playerFor(memberId).discard(actionCardToDiscard, actionCardDeck);
    }

    public void playCard(MemberId memberId, ActionCard actionCardToPlay) {
        playerFor(memberId).playCard(actionCardToPlay);
    }

    public void drawActionCard(MemberId memberId) {
        playerFor(memberId).drawCardFrom(actionCardDeck);
    }

    public void drawTestResultsCard(MemberId memberId) {
        TestResultsCard drawnCard = testResultsCardDeck.draw();

        PlayerEvent event = new PlayerDrewTestResultsCard(memberId, drawnCard);

        enqueue(event);
    }

    public Player playerFor(MemberId memberId) {
        if (!playerMap.containsKey(memberId)) {
            throw new IllegalStateException("Member ID " + memberId + " is not in the '" + handle + "' game.");
        }
        return playerMap.get(memberId);
    }

    public DeckView<ActionCard> actionCardDeck() {
        return actionCardDeck.view();
    }

    public DeckView<TestResultsCard> testResultsCardDeck() {
        return testResultsCardDeck.view();
    }

    public State state() {
        return state;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game game)) {
            return false;
        }

        return handle.equals(game.handle);
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Game.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("handle='" + handle + "'")
                .add("playerMap=" + playerMap)
                .toString();
    }

    public enum State {
        WAITING_TO_START,
        IN_PROGRESS
    }

    public static class GameFactory {

        private final Deck.Shuffler<ActionCard> configuredActionCardShuffler;
        private final CardsFactory configuredCardsFactory;

        public GameFactory() {
            this(new Deck.RandomShuffler<>());
        }

        private GameFactory(Deck.Shuffler<ActionCard> configuredActionCardShuffler) {
            this(configuredActionCardShuffler, new CardsFactory());
        }

        public GameFactory(Deck.Shuffler<ActionCard> definedCardsShuffler,
                           CardsFactory configuredCardsFactory) {
            this.configuredActionCardShuffler = definedCardsShuffler;
            this.configuredCardsFactory = configuredCardsFactory;
        }

        public static GameFactory forTest(Deck.Shuffler<ActionCard> configuredActionCardShuffler) {
            return new GameFactory(configuredActionCardShuffler);
        }

        public static GameFactory forTest(Deck.Shuffler<ActionCard> configuredActionCardShuffler,
                                          CardsFactory cardsFactory) {
            return new GameFactory(configuredActionCardShuffler, cardsFactory);
        }

        /**
         * Create a Game for Production, using a Random shuffler
         *
         * @param gameName title of the game
         * @param handle   unique handle for the game
         */
        public Game create(String gameName, String handle) {
            Game game = new Game(configuredActionCardShuffler, configuredCardsFactory);
            game.initialize(gameName, handle);
            return game;
        }

        /**
         * Create a Game by playing back all of the events and applying them.
         * MUST only be called by the repository (store), or tests.
         * Configures the Shuffler as RandomShuffler for production use
         *
         * @param events GameEvents to play back
         */
        public Game reconstitute(List<GameEvent> events) {
            return new Game(events,
                            configuredActionCardShuffler,
                            configuredCardsFactory);
        }

    }
}
