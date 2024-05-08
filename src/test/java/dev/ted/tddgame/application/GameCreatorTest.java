package dev.ted.tddgame.application;

import dev.ted.tddgame.domain.Game;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameCreatorTest {

    @Test
    void newGameCreatedWithGivenName() {
        GameCreator gameCreator = new GameCreator();

        Game createdGame = gameCreator.createNewGame("game name");

        assertThat(createdGame.name())
                .isEqualTo("game name");
    }


    @Test
    void gamesAssignedUniqueHandle() {
        GameCreator gameCreator = new GameCreator();
        Game game1 = gameCreator.createNewGame("TDD Game");
        Game game2 = gameCreator.createNewGame("TDD Game");

        assertThat(game1.handle())
                .isNotEqualTo(game2.handle());
    }
}