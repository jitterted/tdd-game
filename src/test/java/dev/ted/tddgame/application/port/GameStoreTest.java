package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameStoreTest {

    @Test
    void savedGameCanBeFoundByHandle() {
        GameStore gameStore = new GameStore();
        Game game = Game.create("Game Name", "gameHandle");
        gameStore.save(game);

        assertThat(gameStore.findByHandle("gameHandle"))
                .isEqualTo(game);
    }
}