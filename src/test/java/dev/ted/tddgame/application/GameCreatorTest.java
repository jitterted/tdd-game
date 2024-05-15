package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameViewLoader;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameView;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameCreatorTest {

    @Test
    void newGameCreatedWithGivenName() {
        GameCreator gameCreator = GameCreator.create();

        Game createdGame = gameCreator.createNewGame("game name");

        assertThat(createdGame.name())
                .isEqualTo("game name");
    }


    @Test
    void gamesAssignedUniqueHandle() {
        GameCreator gameCreator = GameCreator.create();
        Game game1 = gameCreator.createNewGame("TDD Game");
        Game game2 = gameCreator.createNewGame("TDD Game");

        assertThat(game1.handle())
                .isNotEqualTo(game2.handle());
    }


    @Test
    @Disabled("Until Game event sourcing, with READ MODEL is completed")
    void gameViewLoaderReturnsViewOfNewGame() {
        GameViewLoader gameViewLoader = new GameViewLoader();
        GameCreator gameCreator = GameCreator.create();

        Game game = gameCreator.createNewGame("new game");

        assertThat(gameViewLoader.findAll())
                .containsExactly(new GameView(game.name(), game.handle(), 0));
    }

}