package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;

// Application Use Case (aka Inbound Port) Command
// Join = Member Selected Game from Lobby
public class PlayerJoinsGame {

    private final GameStore gameStore;
    private final GameFinder gameFinder;

    public PlayerJoinsGame(GameStore gameStore) {
        this.gameStore = gameStore;
        this.gameFinder = new GameFinder(gameStore);
    }

    public static PlayerJoinsGame createNull() {
        return new PlayerJoinsGame(GameStore.createEmpty());
    }

    public void join(MemberId memberId, String gameHandle, String playerName) {
        Game game = gameFinder.byHandle(gameHandle);
        game.join(memberId, playerName);
        gameStore.save(game);
    }
}
