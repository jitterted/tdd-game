package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import org.junit.jupiter.api.Disabled;
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
        MockMvcTester mvc = mvcTesterFor(fixture.gameStore);
        mvc.post().uri("/game/game4discard/start-game").exchange();

        mvc.get()
           .uri("/game/handle-of-game/card-menu/PREDICT")
           .assertThat()
           .hasStatus2xxSuccessful();
    }

    @Test
    void postToDiscardEndpointReturns204NoContent() {
        String gameHandle = "game4discard";
        Fixture fixture = createFixture(gameHandle);
        MockMvcTester mvc = mvcTesterFor(fixture.gameStore, fixture.memberStore);
        mvc.post().uri("/game/game4discard/start-game").exchange();

        mvc.post()
           .principal(() -> fixture.memberAuthName)
           .uri("/game/game4discard/cards/discard/LESS_CODE")
           .assertThat()
           .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @Disabled("Until play() is working in the Domain")
    void postToPlayCardEndpointReturns204NoContent() {
        String gameHandle = "game4play";
        Fixture fixture = createFixture(gameHandle);
        MockMvcTester mvc = mvcTesterFor(fixture.gameStore, fixture.memberStore);
        mvc.post().uri("/game/game4play/start-game").exchange();

        mvc.post()
           .principal(() -> fixture.memberAuthName)
           .uri("/game/game4play/cards/play/LESS_CODE")
           .assertThat()
           .hasStatus(HttpStatus.NO_CONTENT);
    }

    // -- FIXTURE

    private static MockMvcTester mvcTesterFor(GameStore fixture) {
        return mvcTesterFor(fixture, new MemberStore());
    }

    private static MockMvcTester mvcTesterFor(GameStore fixture, MemberStore memberStore) {
        return MockMvcTester.of(
                new PlayingGame(fixture, new GamePlay(
                        fixture, new GamePlayTest.NoOpDummyBroadcaster()
                ), memberStore)
        );
    }

    private static Fixture createFixture(String handle) {
        MemberStore memberStore = new MemberStore();
        Member member = new Member(new MemberId(46L), "IRRELEVANT nickname", "test-auth-username");
        memberStore.save(member);
        GameStore gameStore = GameStore.createEmpty();
        Game game = Game.create("Game to be Started", handle);
        game.join(member.id(), "Player 1");
        gameStore.save(game);
        return new Fixture(gameStore, game, memberStore, member.authName());
    }

    private record Fixture(GameStore gameStore, Game game, MemberStore memberStore, String memberAuthName) {}

}