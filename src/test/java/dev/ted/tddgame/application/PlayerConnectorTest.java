package dev.ted.tddgame.application;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.port.ForTrackingPlayerMessageSenders;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.*;

class PlayerConnectorTest {

    @Test
    void playerConnectedMessageBroadcastToAllWhenPlayerConnectsToGame() {
        String playerUsername = "greenusername";
        Member member = new Member(new MemberId(17L), "Green Member Name", playerUsername);
        String gameHandle = "sassy-dog-35";
        Game game = Game.create("Name of Game", gameHandle);
        game.join(member.id(), "Green Player Name");
        Player player = game.playerFor(member.id());
        MockBroadcaster mockBroadcaster = new MockBroadcaster(game, player);
        MessageSender messageSender = new DummyMessageSender();
        MockForTrackingPlayerMessageSenders mockTrackingPlayerMessageSenders =
                new MockForTrackingPlayerMessageSenders(messageSender, gameHandle, player.id());
        PlayerConnector playerConnector = new PlayerConnector(mockBroadcaster,
                                                              MemberFinder.createNull(member),
                                                              GameFinder.createNull(game),
                                                              mockTrackingPlayerMessageSenders);

        playerConnector.connect(playerUsername, gameHandle, messageSender);

        mockBroadcaster.verify();
        mockTrackingPlayerMessageSenders.verify();
    }

    private static class MockBroadcaster extends GamePlayTest.CrashTestDummyBroadcaster {
        private final Game expectedGame;
        private final Player expectedPlayer;
        private boolean playerConnectedToGameWasCalled;
        private Game actualGame;
        private Player actualPlayer;

        public MockBroadcaster(Game expectedGame, Player expectedPlayer) {
            this.expectedGame = expectedGame;
            this.expectedPlayer = expectedPlayer;
        }

        public void verify() {
            assertThat(playerConnectedToGameWasCalled)
                    .as("playerConnectedToGame() was not called")
                    .isTrue();
            assertThat(actualGame)
                    .isEqualTo(expectedGame);
            assertThat(actualPlayer)
                    .isEqualTo(expectedPlayer);
        }

        @Override
        public void announcePlayerConnectedToGame(Game game, Player player) {
            playerConnectedToGameWasCalled = true;
            this.actualGame = game;
            this.actualPlayer = player;
        }

        @Override
        public void prepareForGamePlay(Game game) {
            throw new IllegalStateException("clearStartgameModal should never be called during Player Connection");
        }
    }

    private static class MockForTrackingPlayerMessageSenders implements ForTrackingPlayerMessageSenders {
        private final MessageSender expectedMessageSender;
        private final String expectedGameHandle;
        private final PlayerId expectedPlayerId;
        private MessageSender actualMessageSender;
        private String actualGameHandle;
        private PlayerId actualPlayerId;
        private boolean addWasCalled;

        public MockForTrackingPlayerMessageSenders(MessageSender expectedMessageSender, String expectedGameHandle, PlayerId expectedPlayerId) {
            this.expectedMessageSender = expectedMessageSender;
            this.expectedGameHandle = expectedGameHandle;
            this.expectedPlayerId = expectedPlayerId;
        }

        @Override
        public void add(MessageSender messageSender, String gameHandle, PlayerId playerId) {
            addWasCalled = true;
            actualMessageSender = messageSender;
            actualGameHandle = gameHandle;
            actualPlayerId = playerId;
        }

        @Override
        public void remove(WebSocketSession webSocketSession) {}

        public void verify() {
            assertThat(addWasCalled)
                    .as("add() was never called")
                    .isTrue();
            assertThat(actualMessageSender)
                    .as("MessageSender was not what we passed in")
                    .isEqualTo(expectedMessageSender);
            assertThat(actualGameHandle)
                    .as("GameHandle was not what we passed in")
                    .isEqualTo(expectedGameHandle);
            assertThat(actualPlayerId)
                    .as("PlayerId was not what we passed in")
                    .isEqualTo(expectedPlayerId);
        }
    }

    private static class DummyMessageSender implements MessageSender {

        @Override
        public void sendMessage(String message) {}
    }
}