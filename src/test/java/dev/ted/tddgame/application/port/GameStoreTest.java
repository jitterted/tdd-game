package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameEvent;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameStoreTest {

    @Test
    void emptyOptionalWhenHandleNotFound() {
        GameStore gameStore = GameStore.createEmpty();

        assertThat(gameStore.findByHandle("doesNotExist"))
                .isEmpty();
    }

    @Test
    void savedGameCanBeFoundByHandle() {
        GameStore gameStore = GameStore.createEmpty();
        Game game = new Game.GameFactory().create("Game Name", "sad-beaver-92");
        gameStore.save(game);

        assertThat(gameStore.findByHandle("sad-beaver-92"))
                .contains(game);
    }

    @Test
    void saveAppendsFreshEventsAndKeepsReconstitutedEvents() {
        GameStore gameStore = GameStore.createEmpty();
        Game game = new Game.GameFactory().create("Game Name", "sleepy-mouse-33");
        gameStore.save(game);

        Game loadedGame = gameStore.findByHandle("sleepy-mouse-33").orElseThrow();
        loadedGame.join(new MemberId(12L), "Alice");
        gameStore.save(loadedGame);

        Game reloadedGame = gameStore.findByHandle("sleepy-mouse-33").orElseThrow();
        reloadedGame.join(new MemberId(13L), "Bob");
        gameStore.save(reloadedGame);

        Game reconstitutedGame = gameStore.findByHandle("sleepy-mouse-33").orElseThrow();

        assertThat(reconstitutedGame.name())
                .isEqualTo("Game Name");
        assertThat(reconstitutedGame.players())
                .extracting(Player::memberId, Player::playerName)
                .containsExactly(
                        tuple(new MemberId(12L), "Alice"),
                        tuple(new MemberId(13L), "Bob")
                );
    }

    @Test
    void findUsesGameFactoryToReconstituteGame() {
        GameStore gameStore = GameStore.createEmpty(new Game.GameFactory() {
            @Override
            public @NonNull Game reconstitute(@NonNull List<GameEvent> events) {
                return new Game.GameFactory().create("This Game Was Created by the GameFactory",
                                                "game-as-saved");
            }
        });
        Game game = new Game.GameFactory().create("This Game Was Saved", "game-as-saved");
        gameStore.save(game);

        assertThat(gameStore.findByHandle("game-as-saved"))
                .isPresent()
                .get()
                .extracting(Game::name, STRING)
                .isEqualTo("This Game Was Created by the GameFactory");
    }

}