package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.TestResultsCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.security.Principal;

import static dev.ted.tddgame.adapter.HtmlElement.attributes;
import static dev.ted.tddgame.adapter.HtmlElement.text;
import static org.assertj.core.api.Assertions.*;

class PlayingGameControllerTest {

    @Test
    void gameReturnsGameViewWithPlayerViews() {
        String gameHandle = "wily-coyote-77";
        Fixture fixture = createGameWithPlayingGameControllerWithRandomShuffler(gameHandle);

        Model model = new ConcurrentModel();
        fixture.playingGameController().game(model, gameHandle);

        GameView gameView = (GameView) model.getAttribute("gameView");

        Game game = fixture.findGame(gameHandle);
        assertThat(gameView)
                .isEqualTo(new GameView(gameHandle,
                                        PlayerView.from(game.players())));
    }

    @Test
    void cardMenuPopulatedWithButtonsForDiscardAndPlay() {
        String gameHandle = "bugs-bunny-33";
        String cardName = "CODE_BLOAT";
        Fixture fixture = createGameWithPlayingGameControllerWithRandomShuffler(gameHandle);

        String html = fixture.playingGameController.cardMenu(gameHandle, cardName);

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

    @Test
    void discardCardRequestMustDiscardSpecifiedCardFromPlayerHand() {
        String gameHandle = "discard-game-handle";
        Fixture fixture = createGameWithPlayingGameControllerWithRandomShuffler(gameHandle);
        fixture.gamePlay.start(gameHandle);
        Game game = fixture.findGame(gameHandle);

        ActionCard cardFromFirstPlayerHand = firstCardFromFirstPlayerHand(game);

        fixture.playingGameController.discardCardFromHand(fixture.principal,
                                                          gameHandle,
                                                          cardFromFirstPlayerHand.name());

        game = fixture.findGame(gameHandle);
        assertThat(game.actionCardDeck().discardPile())
                .containsExactly(cardFromFirstPlayerHand);
    }

    @Test
    void writeCodeInWorkspaceWhenWriteCodeCardPlayedOnWriteCodeForTestTile() {
        String gameHandle = "play-game-handle";
        GameScenarioBuilder builder = GameScenarioBuilder
                .create(gameHandle)
                .actionCards(
                        ActionCard.WRITE_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.PREDICT,
                        ActionCard.PREDICT)
                .memberJoinsAsOnlyPlayer()
                .startGame()
                .discard(ActionCard.LESS_CODE)
                .discard(ActionCard.LESS_CODE);

        builder.playingGameController()
               .playCard(builder.firstPlayerPrincipal(),
                         gameHandle,
                         ActionCard.WRITE_CODE.name());

        assertThat(builder.firstPlayer().hand())
                .as("Write Code card was played, so should not be in their hand")
                .containsExactly(ActionCard.PREDICT,
                                 ActionCard.PREDICT);
    }

    @Test
    void drawActionCardMovesTopCardFromDrawPileToPlayerHand() {
        ActionCard cardToBeDrawn = ActionCard.REFACTOR;
        String gameHandle = "draw-card-scenario";
        GameScenarioBuilder gameScenarioBuilder = GameScenarioBuilder
                .create(gameHandle)
                .actionCards(
                        ActionCard.WRITE_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.PREDICT,
                        ActionCard.PREDICT,
                        cardToBeDrawn)
                .memberJoinsAsOnlyPlayer()
                .startGame()
                .discard(ActionCard.LESS_CODE);

        gameScenarioBuilder.playingGameController()
                           .drawActionCard(
                                   gameScenarioBuilder.firstPlayerPrincipal(),
                                   gameHandle);

        assertThat(gameScenarioBuilder.firstPlayer().hand())
                .as("Player's hand must have the card to be drawn")
                .contains(cardToBeDrawn);
        assertThat(gameScenarioBuilder.game().actionCardDeck().drawPile())
                .isEmpty();
    }

    @Test
    @Disabled("PlayingGameControllerTest 2/28/25 13:54 — until we defer creation of CardsFactory in the GameScenarioBuilder")
    void drawTestResultsCardMovesTopCardFromDrawPileToPlayerWorkspace() {
        TestResultsCard cardToBeDrawn = TestResultsCard.NEED_ONE_LESS_CODE;
        String gameHandle = "draw-test-results-card-scenario";
        GameScenarioBuilder.create(gameHandle)
                           .shuffledActionCards()
                           .testResultsCards(cardToBeDrawn, TestResultsCard.AS_PREDICTED);
    }

    // ---- FIXTURE

    private static Fixture createGameWithPlayingGameControllerWithRandomShuffler(String gameHandle) {
        GameStore gameStore = GameStore.createEmpty(new Game.GameFactory());

        Game game = new Game.GameFactory().create("Only Game In Progress", gameHandle);
        gameStore.save(game);

        MemberStore memberStore = new MemberStore();
        MemberId memberId = new MemberId(32L);
        memberStore.save(new Member(memberId, "BlueNickName", "blueauth"));
        Principal principal = () -> "blueauth"; // implements Principal.getName() = authName

        Broadcaster dummyBroadcaster = new GamePlayTest.NoOpDummyBroadcaster();
        GamePlay gamePlay = new GamePlay(gameStore, dummyBroadcaster);
        PlayingGameController playingGameController =
                new PlayingGameController(gameStore, gamePlay, memberStore);

        // ensure we're going thru the Application layer
        new PlayerJoinsGame(gameStore).join(memberId, gameHandle, "BlueNickName");

        return new Fixture(gameStore, playingGameController, gamePlay, principal, memberId);
    }

    private record Fixture(GameStore gameStore,
                           PlayingGameController playingGameController,
                           GamePlay gamePlay,
                           Principal principal, MemberId memberId) {

        private Game findGame(String gameHandle) {
            return gameStore.findByHandle(gameHandle).orElseThrow();
        }
    }

    private static Player firstPlayerOf(Game game) {
        return game.players().getFirst();
    }

    private static ActionCard firstCardFromFirstPlayerHand(Game game) {
        return firstPlayerOf(game)
                .hand().findFirst()
                .orElseThrow();
    }

}
