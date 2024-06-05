package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;

public class GameFinder {
    private final GameStore gameStore;

    public GameFinder(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    public static GameFinder createNull(Game game) {
        GameStore gameStore = GameStore.createEmpty();
        gameStore.save(game);
        return new GameFinder(gameStore);
    }

    public Game byHandle(String gameHandle) {
        return gameStore.findByHandle(gameHandle)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Game with handle '%s' was not found in the GameStore."
                                        .formatted(gameHandle)
                        ));
    }
}
