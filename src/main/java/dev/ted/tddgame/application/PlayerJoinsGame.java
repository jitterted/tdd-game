package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.PersonId;

// Application Use Case (aka Inbound Port) Command
public class PlayerJoinsGame {

    private final GameStore gameStore;

    public PlayerJoinsGame(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    public static PlayerJoinsGame createNull() {
        return new PlayerJoinsGame(new GameStore());
    }

    public void join(PersonId personId, String gameHandle) {
        Game game = gameStore.findByHandle(gameHandle).orElseThrow();
        game.join(personId);
        gameStore.save(game);
    }
}
