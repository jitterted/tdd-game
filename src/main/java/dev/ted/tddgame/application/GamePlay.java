package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.PlayerId;

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

        broadcaster.prepareForGamePlay(game);

        // start game (if not already started!)
        game.start();

        gameStore.save(game);

        broadcaster.gameUpdate(game);
    }

    // sketch: we think this is what we want
    public void discard(String gameHandle,
                        PlayerId playerId,
                        ActionCard cardToDiscard) {
        // find the game
        // DOMAIN: game.discard(playerId, cardToDiscard)
        // save the game state
        // broadcast game update
    }


}
