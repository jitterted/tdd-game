package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.assertj.core.api.Assertions.*;

class GameJoinerTest {

    @Test
    void memberIsPlayerInGameAfterJoinGame() {
        Member member = new Member(new MemberId(89L), "Blue", "blueauth");
        Game game = Game.create("game name", "rush-cat-21");
        GameJoiner gameJoiner = new GameJoiner(PlayerJoinsGame.createNull(game));

        Principal principal = () -> "Blue"; // username
        String redirectPage = gameJoiner.joinGame(principal, "rush-cat-21");

        assertThat(game.players())
                .hasSize(1)
                .extracting(Player::memberId)
                .containsExactly(new MemberId(42L));

        assertThat(redirectPage)
                .isEqualTo("redirect:/game-in-progress");
    }

}