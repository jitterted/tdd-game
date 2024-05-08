package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String name;
    private String handle;
    private List<Player> players = new ArrayList<>();

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
        return players;
    }

    public Player join(Person person) {
        Player player = new Player(1L, person.id());
        players.add(player);
        return player;
    }
}
