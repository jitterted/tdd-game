package dev.ted.tddgame.domain;

public class Game {
    private String name;
    private String handle;

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
}
