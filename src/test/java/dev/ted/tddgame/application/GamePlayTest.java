package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GamePlayTest {

    @Test
    void startSendsClearModalViaBroadcaster() {
        Member member = new Member(new MemberId(17L), "Green Member Name", "green-username");
        String gameHandle = "sassy-dog-35";
        Game game = Game.create("Name of Game", gameHandle);
        game.join(member.id(), "Green Player Name");

        GameStore gameStore = GameStore.createEmpty();
        gameStore.save(game);
        MockBroadcaster mockBroadcaster = new MockBroadcaster(game);
        GamePlay gamePlay = new GamePlay(gameStore, mockBroadcaster);

        gamePlay.start(gameHandle);

        mockBroadcaster.verify();
    }


    static class MockBroadcaster implements Broadcaster {
        private final Game expectedGame;
        private Game lastGameCleared = null;
        private boolean clearStartGameModalWasInvoked = false;

        public MockBroadcaster(Game expectedGame) {
            this.expectedGame = expectedGame;
        }

        @Override
        public void announcePlayerConnectedToGame(Game game, Player player) {
            throw new IllegalStateException("announcePlayerConnectedToGame should NOT have been invoked during start of game.");
        }

        @Override
        public void clearStartGameModal(Game game) {
            clearStartGameModalWasInvoked = true;
            lastGameCleared = game;
        }

        public void verify() {
            assertThat(clearStartGameModalWasInvoked)
                    .as("Expected the clearStartGameModal() to be invoked")
                    .isTrue();
            assertThat(lastGameCleared)
                    .as("Game cleared was NOT the same as the expected game")
                    .isEqualTo(expectedGame);
        }
    }
}