package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameStoreTest {

    @Test
    void emptyOptionalWhenHandleNotFound() {
        GameStore gameStore = new GameStore();

        assertThat(gameStore.findByHandle("doesNotExist"))
                .isEmpty();
    }

    @Test
    void savedGameCanBeFoundByHandle() {
        GameStore gameStore = new GameStore();
        Game game = Game.create("Game Name", "gameHandle");
        gameStore.save(game);

        assertThat(gameStore.findByHandle("gameHandle"))
                .contains(game);
    }

    @Test
    void saveAppendsFreshEventsAndKeepsReconstitutedEvents() {
        GameStore gameStore = new GameStore();
        Game game = Game.create("Game Name", "sleepy-mouse-33");
        gameStore.save(game);

        Game loadedGame = gameStore.findByHandle("sleepy-mouse-33").orElseThrow();
        loadedGame.join(new MemberId(12L), "Alice");
        gameStore.save(loadedGame);

        Game reconstitutedGame = gameStore.findByHandle("sleepy-mouse-33").orElseThrow();

        assertThat(reconstitutedGame.players())
                .extracting(Player::memberId, Player::playerName)
                .containsExactly(tuple(new MemberId(12L), "Alice"));
    }
}