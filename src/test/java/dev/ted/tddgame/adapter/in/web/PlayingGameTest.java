package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayingGameTest {

    @Test
    @Disabled("Working on redirect to /game")
    void gameReturnsGameViewWithPlayerViews() {
        GameStore gameStore = new GameStore();
        Game game = Game.create("Only Game In Progress", "wily-coyote-77");
        MemberStore memberStore = new MemberStore();
        memberStore.save(new Member(new MemberId(32L), "BlueNickName", "blueauth"));
        game.join(new MemberId(32L), "BlueNickName");
        gameStore.save(game);
        PlayingGame playingGame = new PlayingGame(gameStore);

        ConcurrentModel model = new ConcurrentModel();
        playingGame.game(model);

        GameView gameView = (GameView) model.getAttribute("gameView");

        assertThat(gameView)
                .isEqualTo(new GameView(game.handle(),
                                        from(game.players())));
    }

    private List<PlayerView> from(List<Player> players) {
        return players.stream()
                      .map(player -> new PlayerView(player.playerName()))
                      .toList();
    }
}