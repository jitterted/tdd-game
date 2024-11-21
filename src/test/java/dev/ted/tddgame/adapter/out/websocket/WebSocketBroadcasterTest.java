package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.adapter.shared.PlayerConnections;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class WebSocketBroadcasterTest {

    @Test
    void htmlSentToAllConnectedPlayersUponPlayerConnected() {
        Game game = Game.create("irrelevant game name", "gameHandle");
        MemberId memberId = new MemberId(78L);
        game.join(memberId, "Oliver");
        Player player = game.playerFor(memberId);
        PlayerConnections playerConnections = new PlayerConnections();
        MessageSenderSpy messageSender = new MessageSenderSpy();
        playerConnections.connect(messageSender, "gameHandle");
        WebSocketBroadcaster broadcaster = new WebSocketBroadcaster(playerConnections);
        game.start();

        broadcaster.announcePlayerConnectedToGame(game, player);

        // expect HTML sent to all connected sessions
        assertThat(messageSender.lastSentMessage())
                .as("Last sent message should not be null, i.e., was never called")
                .isNotNull()
                .isNotEmpty();
    }


    @Disabled("Until we have a way to access player's outbound text")
    @Test
    void playerSpecificHtmlSentUponGameUpdate() {
        Game game = Game.create("irrelevant game name", "gameHandle");
        game.start();
        MemberId memberId = new MemberId(78L);
        game.join(memberId, "Oliver");
        Player player = game.playerFor(memberId);
        WebSocketBroadcaster broadcaster = new WebSocketBroadcaster(new PlayerConnections());
        broadcaster.announcePlayerConnectedToGame(game, player);

        broadcaster.gameUpdate(game);

        // then HTML (customized for Player1) is sent to Player1
        // how to verify?
        fail("need to check the player-tailored HTML");
    }

    private static class MessageSenderSpy implements MessageSender {
        public String lastSentMessage() {
            return sentMessage;
        }

        private String sentMessage;

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void sendMessage(String message) {
            sentMessage = message;
        }
    }
}