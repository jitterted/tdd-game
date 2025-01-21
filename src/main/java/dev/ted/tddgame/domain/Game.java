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
    private final DeckFactory deckFactory = new DeckFactory();
    private ActionCardDeck actionCardDeck;

    private Game() {
    }

    // Rebuilds Game (and its entities) state
    // Public production code should use #reconstitute()
    private Game(List<GameEvent> events) {
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
                this.state = State.WAITING_TO_START;
            }

            case PlayerJoined(MemberId memberId, String playerName) ->
                    playerMap.computeIfAbsent(memberId,
                                              _ -> createPlayer(memberId, playerName));

            case GameStarted _ -> this.state = State.IN_PROGRESS;

            case PlayerEvent playerEvent -> playerFor(playerEvent.memberId())
                    .apply(playerEvent);

            case ActionCardDeckCreated(List<ActionCard> actionCards) ->
                    actionCardDeck = ActionCardDeck.create(actionCards, this::enqueue);

            case DeckEvent deckEvent -> actionCardDeck.apply(deckEvent);
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
        enqueue(new ActionCardDeckCreated(deckFactory.createStandardActionCards()));

        players().forEach(player -> player.drawToFullFrom(actionCardDeck));
    }

    public void discard(MemberId memberId, ActionCard actionCardToDiscard) {
        playerFor(memberId).discard(actionCardToDiscard, actionCardDeck);
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

    public State state() {
        return state;
    }

    public enum State {
        WAITING_TO_START,
        IN_PROGRESS
    }
}
