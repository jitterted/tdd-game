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

    public Player join(Person person) {
        return playerMap.computeIfAbsent(
                person.id(),
                personId ->
                        new Player(playerIdGenerator.getAndIncrement(), personId));
    }

}
