package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@Tag("mvc")
class PlayingGameMvcTest {

    @Test
    void getToGameEndpointReturns200() {
        GameStore gameStore = GameStore.createEmpty();
        gameStore.save(Game.create("Game Name", "get-game-handle"));

        MockMvcTester mvc = MockMvcTester.of(
                new PlayingGame(gameStore, new GamePlay(
                        gameStore, new GamePlayTest.NoOpDummyBroadcaster())));

        mvc.get()
           .uri("/game/get-game-handle")
           .assertThat()
           .hasStatus2xxSuccessful();
    }

    @Test
    void postToStartEndpointReturns204() throws Exception {
        GameStore gameStore = GameStore.createEmpty();
        Game game = Game.create("Game to be Started", "game2start");
        game.join(new MemberId(1L), "Player 1");
        gameStore.save(game);

        MockMvcTester mvc = MockMvcTester.of(
                new PlayingGame(gameStore, new GamePlay(
                        gameStore, new GamePlayTest.NoOpDummyBroadcaster())));

        mvc.post()
           .uri("/game/game2start/start-game")
           .assertThat()
           .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void getToCardMenuEndpointReturns200StatusOk() {
        GameStore gameStore = GameStore.createEmpty();
        Game game = Game.create("Game to be Started", "handle-of-game");
        game.join(new MemberId(1L), "Player 1");
        game.start();
        gameStore.save(game);

        MockMvcTester mvc = MockMvcTester.of(
                new PlayingGame(gameStore, new GamePlay(
                        gameStore, new GamePlayTest.NoOpDummyBroadcaster())));

        mvc.get()
           .uri("/game/handle-of-game/card-menu/PREDICT")
           .assertThat()
           .hasStatus2xxSuccessful();
    }

}