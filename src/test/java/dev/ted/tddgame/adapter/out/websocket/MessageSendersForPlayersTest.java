package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MessageSendersForPlayersTest {

    @Test
    void canSendHtmlToSpecificPlayerInGame() {
        MessageSendersForPlayers messageSendersForPlayers = new MessageSendersForPlayers();
        messageSendersForPlayers.add(new MessageSenderCrashDummy("player 1"), "gameHandle", new PlayerId(91L));
        PlayerId player2Id = new PlayerId(92L);
        String messageForPlayer2 = "WebSocket Message For Player 2";
        MockMessageSender player2Sender = new MockMessageSender(messageForPlayer2);
        messageSendersForPlayers.add(player2Sender, "gameHandle", player2Id);
        messageSendersForPlayers.add(new MessageSenderCrashDummy("player 3"), "gameHandle", new PlayerId(93L));

        messageSendersForPlayers.sendTo("gameHandle", player2Id, messageForPlayer2);

        player2Sender.verifyMessageSent();
    }

    @Test
    void sentToAllSendsMessageToAllPlayersInSpecificGame() {
        MessageSendersForPlayers messageSendersForPlayers = new MessageSendersForPlayers();
        String message = "WebSocket Message For All Players";
        MockMessageSender messageSender1 = new MockMessageSender(message);
        messageSendersForPlayers.add(messageSender1, "test-game-42", new PlayerId(91L));
        MockMessageSender messageSender2 = new MockMessageSender(message);
        messageSendersForPlayers.add(messageSender2, "test-game-42", new PlayerId(92L));

        messageSendersForPlayers.sendToAll("test-game-42", message);

        messageSender1.verifyMessageSent();
        messageSender2.verifyMessageSent();
    }

    private static class MessageSenderCrashDummy implements MessageSender {
        private final String playerName;

        public MessageSenderCrashDummy(String playerName) {
            this.playerName = playerName;
        }

        @Override
        public void sendMessage(String message) {
            throw new IllegalStateException("Should not have called sendMessage(\"" + message + "\") for player: " + playerName);
        }
    }

    private static class MockMessageSender implements MessageSender {
        private String actualSentMessage;
        private final String expectedMessage;

        public MockMessageSender(String expectedMessage) {
            this.expectedMessage = expectedMessage;
        }

        @Override
        public void sendMessage(String message) {
            actualSentMessage = message;
        }

        private void verifyMessageSent() {
            assertThat(actualSentMessage)
                    .as("No message was sent, but expected: " + expectedMessage)
                    .isNotNull()
                    .as("Sent a different message than expected")
                    .isEqualTo(expectedMessage);
        }
    }
}