package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Disabled("Until EventDto to/from Event has covered all GameEvents")
class GamePlayTest {

    @Test
    void startSendsClearModalViaBroadcaster() {
        GameFixture gameFixture = createGameStoreWithGameWithOnePlayer();
        MockBroadcaster mockBroadcaster = new MockBroadcaster(gameFixture.game());
        GamePlay gamePlay = new GamePlay(gameFixture.gameStore(), mockBroadcaster);

        gamePlay.start(gameFixture.gameHandle());

        mockBroadcaster.verify();
    }

    @Test
    void startTellsGameToStart() {
        GameFixture gameFixture = createGameStoreWithGameWithOnePlayer();

        GamePlay gamePlay = new GamePlay(gameFixture.gameStore(),
                                         new DummyBroadcaster());

        gamePlay.start(gameFixture.gameHandle);

        Game game = gameFixture.gameStore()
                               .findByHandle(gameFixture.gameHandle())
                               .orElseThrow();
        assertThat(game.freshEvents())
                .contains(new GameStarted());

    }

    private static GameFixture createGameStoreWithGameWithOnePlayer() {
        Member member = new Member(new MemberId(17L), "Green Member Name", "green-username");
        String gameHandle = "sassy-dog-35";
        Game game = Game.create("Name of Game", gameHandle);
        game.join(member.id(), "Green Player Name");

        GameStore gameStore = GameStore.createEmpty();
        gameStore.save(game);
        return new GameFixture(gameHandle, game, gameStore);
    }

    private record GameFixture(String gameHandle, Game game, GameStore gameStore) {
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

    private static class DummyBroadcaster implements Broadcaster {
        @Override
        public void announcePlayerConnectedToGame(Game game, Player player) {
        }

        @Override
        public void clearStartGameModal(Game game) {
        }
    }
}