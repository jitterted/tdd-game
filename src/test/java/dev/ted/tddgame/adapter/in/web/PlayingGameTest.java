package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static dev.ted.tddgame.adapter.HtmlElement.attributes;
import static dev.ted.tddgame.adapter.HtmlElement.text;
import static org.assertj.core.api.Assertions.*;

class PlayingGameTest {

    @Test
    void getToCardMenuEndpointReturns200StatusOk() {
        GameStore gameStore = GameStore.createEmpty();
        Game game = Game.create("Game to be Started", "handle-of-game");
        game.join(new MemberId(1L), "Player 1");
        game.start();
        gameStore.save(game);

        MockMvcTester mvc = MockMvcTester.of(new PlayingGame(gameStore, new GamePlay(gameStore, new GamePlayTest.NoOpDummyBroadcaster())));

        mvc.get()
           .uri("/game/handle-of-game/card-menu/PREDICT")
           .assertThat()
           .hasStatus2xxSuccessful();
    }

    @Test
    void gameReturnsGameViewWithPlayerViews() {
        String gameHandle = "wily-coyote-77";
        Fixture fixture = createGameWithPlayingGameController(gameHandle);

        Model model = new ConcurrentModel();
        fixture.playingGame().game(model, gameHandle);

        GameView gameView = (GameView) model.getAttribute("gameView");

        assertThat(gameView)
                .isEqualTo(new GameView(gameHandle,
                                        PlayerView.from(fixture.game().players())));
    }

    @Test
    void cardMenuPopulatedWithButtonsForDiscardAndPlay() {
        String gameHandle = "bugs-bunny-33";
        String cardName = "CODE_BLOAT";
        Fixture fixture = createGameWithPlayingGameController(gameHandle);

        String html = fixture.playingGame.cardMenu(gameHandle, cardName);

        assertThat(html)
                .isEqualTo(
                        HtmlElement.swapInnerHtml(
                                "dialog",
                                HtmlElement.div(
                                        "",
                                        HtmlElement.button(
                                                attributes()
                                                        .autofocus()
                                                        .hxPost("/game/bugs-bunny-33/cards/play/CODE_BLOAT")
                                                        .hxOn("before-request", "document.querySelector('dialog').close()"),
                                                text("Play Card into Workspace")
                                        )
                                ),
                                HtmlElement.div(
                                        "",
                                        HtmlElement.button(
                                                attributes()
                                                        .hxPost("/game/bugs-bunny-33/cards/discard/CODE_BLOAT")
                                                        .hxOn("before-request", "document.querySelector('dialog').close()"),
                                                text("Discard Card to Discard Pile")
                                        )
                                )
                        ).render()
                );
    }

    // ---- FIXTURE

    private static Fixture createGameWithPlayingGameController(String gameHandle) {
        GameStore gameStore = GameStore.createEmpty();
        Game game = Game.create("Only Game In Progress", gameHandle);
        MemberStore memberStore = new MemberStore();
        memberStore.save(new Member(new MemberId(32L), "BlueNickName", "blueauth"));
        game.join(new MemberId(32L), "BlueNickName");
        gameStore.save(game);
        Broadcaster dummyBroadcaster = new GamePlayTest.NoOpDummyBroadcaster();
        GamePlay gamePlay = new GamePlay(gameStore, dummyBroadcaster);
        PlayingGame playingGame = new PlayingGame(gameStore, gamePlay);
        return new Fixture(game, playingGame);
    }

    private record Fixture(Game game, PlayingGame playingGame) {}

}
