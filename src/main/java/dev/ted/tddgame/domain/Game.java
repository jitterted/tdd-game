package dev.ted.tddgame.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

public class Game extends EventSourcedAggregate {
    private String name;
    private String handle;
    private final Map<MemberId, Player> playerMap = new HashMap<>();
    private final AtomicLong playerIdGenerator = new AtomicLong();

    public Game() {
    }

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
    public void apply(GameEvent event) {
        switch (event) {
            case GameCreated(String name, String handle) -> {
                this.name = name;
                this.handle = handle;
            }
            case PlayerJoined(MemberId memberId, String playerName) -> {
                playerMap.computeIfAbsent(
                        memberId,
                        _ -> new Player(
                                new
                                PlayerId(playerIdGenerator.getAndIncrement()),
                                memberId,
                                playerName));
            }
        }
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
        return playerMap.size() < 4 || playerMap.containsKey(memberId);
    }

    public Player playerFor(MemberId memberId) {
        return playerMap.get(memberId);
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
