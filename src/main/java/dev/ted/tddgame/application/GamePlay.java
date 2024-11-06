package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;

// Application-level Use Case (aka Inbound Port)
// aka COMMAND
public class GamePlay {

    private final GameStore gameStore;
    private final Broadcaster broadcaster;

    public GamePlay(GameStore gameStore, Broadcaster broadcaster) {
        this.gameStore = gameStore;
        this.broadcaster = broadcaster;
    }

    public void start(String gameHandle) {
        Game game = gameStore.findByHandle(gameHandle)
                             .orElseThrow(() -> new RuntimeException(
                                     "Game '%s' not found".formatted(gameHandle)));

        broadcaster.clearStartGameModal(game);

        // start game (if not already started!)
        game.start();

        gameStore.save(game);

        broadcaster.gameUpdate(game);
    }

}
