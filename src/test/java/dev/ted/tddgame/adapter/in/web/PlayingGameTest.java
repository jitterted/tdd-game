package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;

class PlayingGameTest {

    @Test
    void gameReturnsGameViewWithPlayerViews() {
        Fixture fixture = createFixture();
        PlayingGame playingGame = new PlayingGame(fixture.gameStore());

        Model model = new ConcurrentModel();
        playingGame.game(model, fixture.gameHandle());

        GameView gameView = (GameView) model.getAttribute("gameView");

        assertThat(gameView)
                .isEqualTo(new GameView(fixture.game().handle(),
                                        PlayerView.from(fixture.game().players())));
    }

    @Test
    @Disabled("Until freshEvents returns truly fresh events, and the Game implements start game")
    void gameStartedEventGeneratedWhenGameStarted() {
        Fixture fixture = createFixture();
        PlayingGame playingGame = new PlayingGame(fixture.gameStore());

        playingGame.startGame(fixture.gameHandle());

        Game game = fixture.gameStore().findByHandle(fixture.gameHandle()).orElseThrow();
        assertThat(game.freshEvents())
                .containsExactly(new GameStarted());
    }

    private Fixture createFixture() {
        GameStore gameStore = new GameStore();
        String gameHandle = "wily-coyote-77";
        Game game = Game.create("Only Game In Progress", gameHandle);
        MemberStore memberStore = new MemberStore();
        memberStore.save(new Member(new MemberId(32L), "BlueNickName", "blueauth"));
        game.join(new MemberId(32L), "BlueNickName");
        gameStore.save(game);
        return new Fixture(gameStore, gameHandle, game);
    }
    private record Fixture(GameStore gameStore, String gameHandle, Game game) {

    }
}