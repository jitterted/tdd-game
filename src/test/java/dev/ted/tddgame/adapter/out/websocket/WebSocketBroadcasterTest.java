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
        MemberId memberIdForOliver = new MemberId(78L);
        game.join(memberIdForOliver, "Oliver");
        game.join(new MemberId(63L), "Samantha");
        PlayerConnections playerConnections = new PlayerConnections();
        MessageSenderSpy messageSenderForOliver = new MessageSenderSpy();
        playerConnections.connect(messageSenderForOliver, "gameHandle");
        MessageSenderSpy messageSenderForSamantha = new MessageSenderSpy();
        playerConnections.connect(messageSenderForSamantha, "gameHandle");
        WebSocketBroadcaster broadcaster = new WebSocketBroadcaster(playerConnections);
        game.start();

        Player oliverPlayer = game.playerFor(memberIdForOliver);
        broadcaster.announcePlayerConnectedToGame(game, oliverPlayer);

        // expect HTML sent to all connected sessions
        assertThat(messageSenderForOliver.lastSentMessage())
                .as("Last sent message should not be null, i.e., was never called")
                .isNotNull()
                .isNotEmpty();
        assertThat(messageSenderForSamantha.lastSentMessage())
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