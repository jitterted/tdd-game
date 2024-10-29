package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerConnectorTest {

    @Test
    void playerConnectedMessageBroadcastWhenPlayerConnectsToGame() {
        String playerUsername = "greenusername";
        Member member = new Member(new MemberId(17L), "Green Member Name", playerUsername);
        String gameHandle = "sassy-dog-35";
        Game game = Game.create("Name of Game", gameHandle);
        game.join(member.id(), "Green Player Name");
        Player player = game.playerFor(member.id());
        MockBroadcaster mockBroadcaster = new MockBroadcaster(game, player);
        PlayerConnector playerConnector = new PlayerConnector(mockBroadcaster,
                                                              MemberFinder.createNull(member),
                                                              GameFinder.createNull(game));

        playerConnector.connect(playerUsername, gameHandle);

        mockBroadcaster.verify();
    }

    private static class MockBroadcaster implements Broadcaster {
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
        public void clearStartgameModal(Game game) {
        }
    }
}