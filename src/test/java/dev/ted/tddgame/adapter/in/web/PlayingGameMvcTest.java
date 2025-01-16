package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
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

        MockMvcTester mvc = mvcTesterFor(gameStore);

        mvc.get()
           .uri("/game/get-game-handle")
           .assertThat()
           .hasStatus2xxSuccessful();
    }

    @Test
    void postToStartEndpointReturns204NoContent() {
        String gameHandle = "game2start";
        Fixture fixture = createFixture(gameHandle);

        MockMvcTester mvc = mvcTesterFor(fixture.gameStore);

        mvc.post()
           .uri("/game/game2start/start-game")
           .assertThat()
           .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void getToCardMenuEndpointReturns200StatusOk() {
        String handle = "handle-of-game";
        Fixture fixture = createFixture(handle);
        fixture.game.start();

        MockMvcTester mvc = mvcTesterFor(fixture.gameStore);

        mvc.get()
           .uri("/game/handle-of-game/card-menu/PREDICT")
           .assertThat()
           .hasStatus2xxSuccessful();
    }

    @Test
    void postToDiscardEndpointReturns204NoContent() {
        String gameHandle = "game4discard";
        Fixture fixture = createFixture(gameHandle);
        fixture.game.start();

        MockMvcTester mvc = mvcTesterFor(fixture.gameStore);

        mvc.post()
           .uri("/game/game4discard/cards/discard/LESS_CODE")
           .assertThat()
           .hasStatus(HttpStatus.NO_CONTENT);
    }


    // -- FIXTURE

    private static MockMvcTester mvcTesterFor(GameStore fixture) {
        return MockMvcTester.of(
                new PlayingGame(fixture, new GamePlay(
                        fixture, new GamePlayTest.NoOpDummyBroadcaster()
                ), new MemberStore())
        );
    }

    private static Fixture createFixture(String handle) {
        GameStore gameStore = GameStore.createEmpty();
        Game game = Game.create("Game to be Started", handle);
        game.join(new MemberId(1L), "Player 1");
        gameStore.save(game);
        return new Fixture(gameStore, game);
    }

    private record Fixture(GameStore gameStore, Game game) {}

}