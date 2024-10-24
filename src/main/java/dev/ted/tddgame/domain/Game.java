package dev.ted.tddgame.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

public class Game extends EventSourcedAggregate {
    private static final int MAXIMUM_NUMBER_OF_PLAYERS = 4;
    private List<ActionCard> actionCardsForDeck;
    private String name;
    private String handle;
    private final Map<MemberId, Player> playerMap = new HashMap<>();
    private final AtomicLong playerIdGenerator = new AtomicLong();
    private Deck<ActionCard> actionCardDeck;

    private Game() {
    }

    public Game(List<GameEvent> gameEvents,
                ActionCard... actionCards
    ) {
        this(gameEvents);
        this.actionCardsForDeck = List.of(actionCards);
    }

    public static Game configureForTest(List<GameEvent> gameEvents,
                                        ActionCard... actionCards) {
        return new Game(gameEvents, actionCards);
    }

    // for REBUILDING Game (et al) state
    public Game(List<GameEvent> events) {
        for (GameEvent event : events) {
            apply(event);
        }
    }

    public static Game create(String gameName, String handle) {
        Game game = new Game();
        game.initialize(gameName, handle);
        return game;
    }

    public static Game reconstitute(List<GameEvent> events) {
        return new Game(events);
    }

    private void initialize(String gameName, String handle) {
        GameCreated gameCreated = new GameCreated(gameName, handle);
        enqueue(gameCreated);
    }

    @Override
    protected void apply(GameEvent event) {
        switch (event) {
            case GameCreated(String name, String handle) -> {
                this.name = name;
                this.handle = handle;
            }

            case PlayerJoined(MemberId memberId, String playerName) ->
                    playerMap.computeIfAbsent(memberId,
                                              _ -> createPlayer(memberId, playerName));

            case GameStarted _ -> {
            }

            case ActionCardDeckCreated(List<ActionCard> actionCards) ->
                    // TODO: switch to production version for random shuffler
                    actionCardDeck = Deck.createForTest(this::enqueue,
                            actionCards);

            case PlayerEvent playerEvent ->
                    playerFor(playerEvent.memberId())
                            .apply(playerEvent);

            case DeckEvent deckEvent ->
                    actionCardDeck.apply(deckEvent);
        }
    }

    private Player createPlayer(MemberId memberId, String playerName) {
        return new Player(
                new PlayerId(playerIdGenerator.getAndIncrement()),
                memberId,
                playerName,
                this::enqueue);
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

        enqueue(new PlayerJoined(memberId, playerName));
    }

    public boolean canJoin(MemberId memberId) {
        return playerMap.size() < MAXIMUM_NUMBER_OF_PLAYERS || playerMap.containsKey(memberId);
    }

    public void start() {
        enqueue(new GameStarted());
        enqueue(new ActionCardDeckCreated(actionCardsForDeck));

        MemberId firstMemberId = players().iterator().next().memberId();
        Player player = playerFor(firstMemberId);
        player.drawToFullFrom(actionCardDeck);
    }

    public Player playerFor(MemberId memberId) {
        return playerMap.get(memberId);
    }

    public DeckView<ActionCard> actionCardDeck() {
        return actionCardDeck.view();
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
}
