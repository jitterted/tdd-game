package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameEvent;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GamePlayTest {

    @Test
    void startSendsClearModalViaBroadcaster() {
        GameFixture gameFixture = createGameStoreWithGameWithOnePlayer();
        ClearStartGameModalMockBroadcaster mockBroadcaster = new ClearStartGameModalMockBroadcaster(gameFixture.game());
        GamePlay gamePlay = new GamePlay(gameFixture.gameStore(), mockBroadcaster);

        gamePlay.start(gameFixture.gameHandle());

        mockBroadcaster.verify();
    }

    @Test
    void startTellsGameToStart() {
        GameFixture gameFixture = createGameStoreWithGameWithOnePlayer();

        GamePlay gamePlay = new GamePlay(gameFixture.gameStore(),
                                         new NoOpDummyBroadcaster());

        gamePlay.start(gameFixture.gameHandle);

        List<GameEvent> gameEvents = gameFixture
                .gameStore()
                .allEventsFor(gameFixture.gameHandle());
        assertThat(gameEvents)
                .contains(new GameStarted());
    }

    @Test
    @Disabled("Test this at the Game.start() level, not here")
    void doNotStartGameAgainIfAlreadyStarted() {

    }

    @Test
    void startGameBroadcastsGameStartedInformation() {
        GameFixture gameFixture = createGameStoreWithGameWithOnePlayer();
        GameUpdateMockBroadcaster mockBroadcaster =
                new GameUpdateMockBroadcaster(gameFixture.game());
        GamePlay gamePlay = new GamePlay(gameFixture.gameStore(), mockBroadcaster);

        gamePlay.start(gameFixture.gameHandle());

        mockBroadcaster.verify();

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

    static class ClearStartGameModalMockBroadcaster extends CrashTestDummyBroadcaster {

        private final Game expectedGame;
        private Game lastGameCleared = null;
        private boolean clearStartGameModalWasInvoked = false;

        public ClearStartGameModalMockBroadcaster(Game expectedGame) {
            this.expectedGame = expectedGame;
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

    static class CrashTestDummyBroadcaster implements Broadcaster {
        @Override
        public void announcePlayerConnectedToGame(Game game, Player player) {
            throw new IllegalStateException("announcePlayerConnectedToGame should NOT have been invoked.");
        }

        @Override
        public void clearStartGameModal(Game game) {
            throw new IllegalArgumentException("clearStartGameModal should NOT have been invoked.");
        }

        @Override
        public void gameUpdate(Game game) {
            throw new IllegalArgumentException("gameUpdate should NOT have been invoked.");
        }
    }

    public static class NoOpDummyBroadcaster implements Broadcaster {
        @Override
        public void announcePlayerConnectedToGame(Game game, Player player) {
        }

        @Override
        public void clearStartGameModal(Game game) {
        }

        @Override
        public void gameUpdate(Game game) {
        }
    }

    private static class GameUpdateMockBroadcaster extends NoOpDummyBroadcaster {
        private final Game expectedGame;
        private Game actualGameFromUpdate;

        public GameUpdateMockBroadcaster(Game expectedGame) {
            this.expectedGame = expectedGame;
        }

        public void verify() {
            assertThat(actualGameFromUpdate)
                    .as("Game from update was not the expected Game that was started")
                    .isEqualTo(expectedGame);
        }

        @Override
        public void gameUpdate(Game game) {
            actualGameFromUpdate = game;
        }
    }
}