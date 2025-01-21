package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.MessageSender;

import static org.assertj.core.api.Assertions.*;

public class MockMessageSender implements MessageSender {
    private String actualSentMessage;
    private String expectedMessage;

    public MockMessageSender() {
    }

    public MockMessageSender(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    @Override
    public void sendMessage(String message) {
        actualSentMessage = message;
    }

    public void verifyMessageSent() {
        assertThat(expectedMessage)
                .as("Expected Message was not set, can't verify against actual.")
                .isNotNull();
        assertThat(actualSentMessage)
                .as("No message was sent, but expected: " + expectedMessage)
                .isNotNull()
                .as("Sent a different message than expected")
                .isEqualTo(expectedMessage);
    }

    public void verifyNoMessagesSent() {
        assertThat(actualSentMessage)
                .as("Expected no messages sent, but was sent: " + actualSentMessage)
                .isNull();
    }
}
