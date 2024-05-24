package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;

// Application Use Case (aka Inbound Port) Command
public class PlayerJoinsGame {

    private final GameStore gameStore;
    private GameFinder gameFinder;

    public PlayerJoinsGame(GameStore gameStore) {
        this.gameStore = gameStore;
        this.gameFinder = new GameFinder(gameStore);
    }

    public static PlayerJoinsGame createNull() {
        return new PlayerJoinsGame(new GameStore());
    }

    public static PlayerJoinsGame createNull(Game game) {
        GameStore gameStore = new GameStore();
        gameStore.save(game);
        return new PlayerJoinsGame(gameStore);
    }

    public void join(MemberId memberId, String gameHandle, String playerName) {
        Game game = gameFinder.byHandle(gameHandle);
        game.join(memberId, playerName);
        gameStore.save(game);
        // broadcast that the Member has joined the Game as a Player
    }
}
