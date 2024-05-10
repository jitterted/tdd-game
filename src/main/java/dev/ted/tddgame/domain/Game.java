package dev.ted.tddgame.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Game {
    private String name;
    private final String handle;
    private final Map<PersonId, Player> playerMap = new HashMap<>();
    private final AtomicLong playerIdGenerator = new AtomicLong();

    public Game(String name, String handle) {
        this.name = name;
        this.handle = handle;
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
        if (!canJoin()) {
            String ids = playerMap.keySet().stream().map(PersonId::id).toList().toString();
            throw new IllegalStateException("Game is full (Person IDs: " + ids + "), so " + personId + " cannot join.");
        }

        return playerMap.computeIfAbsent(
                personId,
                _ -> new Player(
                        personId, new
                        PlayerId(playerIdGenerator.getAndIncrement())));
    }

    public boolean canJoin() {
        return playerMap.size() < 4;
    }
}
