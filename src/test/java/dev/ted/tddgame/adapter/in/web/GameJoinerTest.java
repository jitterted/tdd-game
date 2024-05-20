package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.assertj.core.api.Assertions.*;

class GameJoinerTest {

    @Test
    @Disabled("Depends on PlayerJoinsGame.join() accepting a Game Handle instead of a Game Object")
    void personIsInGameAfterJoinGame() {
        Game game = Game.create("game name", "rush-cat-21");
        GameJoiner gameJoiner = GameJoiner.createNull();

        Principal principal = () -> "Blue";
        gameJoiner.joinGame(principal, "rush-cat-21");

        assertThat(game.players())
                .hasSize(1)
                .extracting(Player::id)
                .containsExactly(new PlayerId(99L));
    }

}