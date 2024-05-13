package dev.ted.tddgame.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Game extends EventSourcedAggregate {
    private String name;
    private String handle;
    private final Map<PersonId, Player> playerMap = new HashMap<>();
    private final AtomicLong playerIdGenerator = new AtomicLong();

    public Game() {
    }

    @Deprecated
    public Game(String name, String handle) {
        this.name = name;
        this.handle = handle;
    }

    public Game(List<GameEvent> events) {
        for (GameEvent event : events) {
            apply(event);
        }
    }

    public static EventSourcedAggregate create(String gameName) {
        Game game = new Game();
        game.initialize(gameName);
        return game;
    }

    public static Game reconstitute(List<GameEvent> events) {
        return new Game(events);
    }

    private void initialize(String gameName) {
        GameCreated gameCreated = new GameCreated(gameName);
        enqueue(gameCreated);
    }

    @Override
    public void apply(GameEvent event) {
        switch (event) {
            case GameCreated(String name) -> this.name = name;
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

    public Player join(PersonId personId) {
        if (!canJoin(personId)) {
            String ids = playerMap.keySet().stream().map(PersonId::id).toList().toString();
            throw new IllegalStateException("Game is full (Person IDs: " + ids + "), so " + personId + " cannot join.");
        }

        return playerMap.computeIfAbsent(
                personId,
                _ -> new Player(
                        personId, new
                        PlayerId(playerIdGenerator.getAndIncrement())));
    }

    public boolean canJoin(PersonId personId) {
        return playerMap.size() < 4 || playerMap.containsKey(personId);
    }
}
