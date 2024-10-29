package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;

// Application-level Use Case (aka Inbound Port)
public class GamePlay {

    private final GameStore gameStore;

    public GamePlay(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    public void start(String gameHandle) {
        gameStore.findByHandle(gameHandle)
                 .orElseThrow(() -> new RuntimeException("Game '%s' not found"
                                                                 .formatted(gameHandle)))
                 .start();

    }

}
