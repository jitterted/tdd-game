package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;

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
        Game game = loadGameWith(gameHandle);

        broadcaster.prepareForGamePlay(game);

        // start game (if not already started!)
        game.start();

        gameStore.save(game);

        broadcaster.gameUpdate(game);
    }

    public void discard(String gameHandle,
                        MemberId memberId,
                        ActionCard cardToDiscard) {
        Game game = loadGameWith(gameHandle);

        game.discard(memberId, cardToDiscard);

        gameStore.save(game);

        broadcaster.gameUpdate(game);
    }

    public void playCard(String gameHandle, MemberId memberId, ActionCard cardToPlay) {
        Game game = loadGameWith(gameHandle);

        game.playCard(memberId, cardToPlay);

        broadcaster.gameUpdate(game);
    }

    private Game loadGameWith(String gameHandle) {
        return gameStore.findByHandle(gameHandle)
                        .orElseThrow(() -> new RuntimeException(
                                "Game '%s' not found".formatted(gameHandle)));
    }
}
