package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class WebSocketBroadcasterTest {

    @Disabled("Until we have a way to access the outbound HTML")
    @Test
    void htmlSentUponPlayerConnected() {
        Game game = Game.create("irrelevant game name", "gameHandle");
        game.start();
        MemberId memberId = new MemberId(78L);
        game.join(memberId, "Oliver");
        Player player = game.playerFor(memberId);
        WebSocketBroadcaster broadcaster = new WebSocketBroadcaster();

        broadcaster.announcePlayerConnectedToGame(game, player);

        // expect HTML sent to all connected sessions
        fail("need to check the HTML sent");
    }


    @Disabled("Until we have a way to access player's outbound text")
    @Test
    void playerSpecificHtmlSentUponGameUpdate() {
        Game game = Game.create("irrelevant game name", "gameHandle");
        game.start();
        MemberId memberId = new MemberId(78L);
        game.join(memberId, "Oliver");
        Player player = game.playerFor(memberId);
        WebSocketBroadcaster broadcaster = new WebSocketBroadcaster();
        broadcaster.announcePlayerConnectedToGame(game, player);

        broadcaster.gameUpdate(game);

        // then HTML (customized for Player1) is sent to Player1
        // how to verify?
        fail("need to check the player-tailored HTML");
    }

}