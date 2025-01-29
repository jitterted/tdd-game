package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
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
        MemberStore memberStore = new MemberStore();
        Member member = new Member(new MemberId(89L), "BlueNickName", "blueauth");
        memberStore.save(member);
        GameStore gameStore = GameStore.createEmpty();
        String gameHandle = "rush-cat-21";
        Game game = new Game.GameFactory().create("game name", gameHandle);
        gameStore.save(game);
        GameJoiner gameJoiner = new GameJoiner(new PlayerJoinsGame(gameStore),
                                               memberStore);

        Principal principal = () -> "blueauth"; // Principal.getName() = authName
        String redirectPage = gameJoiner.joinGame(principal, gameHandle);

        Game loadedGame = gameStore.findByHandle(gameHandle).orElseThrow();
        assertThat(loadedGame.players())
                .hasSize(1)
                .extracting(Player::memberId, Player::playerName)
                .containsExactly(tuple(new MemberId(89L), "BlueNickName"));

        assertThat(redirectPage)
                .isEqualTo("redirect:/game/rush-cat-21");
    }

}