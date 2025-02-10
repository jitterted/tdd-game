package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;

import java.util.function.Consumer;

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
        execute(gameHandle, game -> {
            broadcaster.prepareForGamePlay(game);
            game.start();
        });
    }

    public void discard(String gameHandle,
                        MemberId memberId,
                        ActionCard cardToDiscard) {
        execute(gameHandle, game ->
                game.discard(memberId, cardToDiscard));
    }

    public void playCard(String gameHandle,
                         MemberId memberId,
                         ActionCard cardToPlay) {
        execute(gameHandle, game ->
                game.playCard(memberId, cardToPlay));
    }

    public void drawActionCard(String gameHandle, MemberId memberId) {
        execute(gameHandle,
                game -> game.drawActionCard(memberId));
    }

    private void execute(String gameHandle, Consumer<Game> command) {
        Game game = loadGameWith(gameHandle);

        command.accept(game);

        gameStore.save(game);

        broadcaster.gameUpdate(game);
    }

    private Game loadGameWith(String gameHandle) {
        return gameStore.findByHandle(gameHandle)
                        .orElseThrow(() -> new RuntimeException(
                                "Game '%s' not found".formatted(gameHandle)));
    }
}
