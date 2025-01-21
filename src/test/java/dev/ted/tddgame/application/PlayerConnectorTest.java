package dev.ted.tddgame.application;

import dev.ted.tddgame.adapter.out.websocket.MessageSendersForPlayers;
import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.ForTrackingPlayerMessageSenders;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerConnectorTest {


    private static GameFixture createTwoMembersInMemberStoreAndGameStoreWithGameWithTwoPlayers() {
        String member1authName = "member1-authname";
        Member member1 = new Member(new MemberId(17L), "Player 1 Member Name", member1authName);
        String member2authName = "member2-authname";
        Member member2 = new Member(new MemberId(3L), "Player 2 Member Name", member2authName);
        MemberFinder memberFinder = MemberFinder.createNull(member1, member2);

        GameStore gameStore = GameStore.createEmpty();
        String gameHandle = "fluffy-cat-13";
        Game game = Game.create("Name of Game", gameHandle);

        game.join(member1.id(), "Player 1 In-Game Name");
        game.join(member2.id(), "Player 2 In-Game Name");

        gameStore.save(game);

        return new GameFixture(gameHandle, gameStore, memberFinder, member1authName, member2authName);
    }

    private record GameFixture(String gameHandle,
                               GameStore gameStore,
                               MemberFinder memberFinder,
                               String member1authName,
                               String member2authName) {
        Game gameFromStore() {
            return gameStore.findByHandle(gameHandle).orElseThrow();
        }
    }

    @Test
    void playerConnectedMessageBroadcastToAllWhenPlayerConnectsToGameFirstTime() {
        String memberUsername = "greenusername";
        Member member = new Member(new MemberId(17L), "Green Member Name", memberUsername);
        String gameHandle = "sassy-dog-35";
        Game game = Game.create("Name of Game", gameHandle);
        game.join(member.id(), "Green Player Name");
        Player player = game.playerFor(member.id());
        MockForInitialConnectionBroadcaster mockForInitialConnectionBroadcaster = new MockForInitialConnectionBroadcaster(game, player);
        MessageSender messageSender = new DummyMessageSender();
        MockForTrackingPlayerMessageSenders mockTrackingPlayerMessageSenders =
                new MockForTrackingPlayerMessageSenders(messageSender, gameHandle, player.id());
        PlayerConnector playerConnector = new PlayerConnector(mockForInitialConnectionBroadcaster,
                                                              MemberFinder.createNull(member),
                                                              GameFinder.createNull(game),
                                                              mockTrackingPlayerMessageSenders);

        playerConnector.connect(memberUsername, gameHandle, messageSender);

        mockForInitialConnectionBroadcaster.verify();
        mockTrackingPlayerMessageSenders.verify();
    }

    @Test
    void playerDisconnectsWhileWaitingForGameToStartSendsUpdates() {

    }

    @Test
    void playerReconnectsWhileWaitingForGameToStartBroadcastsPlayerReconnected() {

    }

    @Test
    void playerReconnectsAfterGameStartedBroadcastRemoveModalAndGameUpdate() {
        GameFixture fixture = createTwoMembersInMemberStoreAndGameStoreWithGameWithTwoPlayers();

        MockReconnectBroadcaster mockBroadcaster = new MockReconnectBroadcaster();
        ForTrackingPlayerMessageSenders forTrackingPlayerMessageSenders = new MessageSendersForPlayers();
        PlayerConnector playerConnector = new PlayerConnector(mockBroadcaster,
                                                              fixture.memberFinder,
                                                              new GameFinder(fixture.gameStore),
                                                              forTrackingPlayerMessageSenders);

        MessageSender messageSender1 = new DummyMessageSender();
        MessageSender messageSender2 = new DummyMessageSender();
        playerConnector.connect(fixture.member1authName, fixture.gameHandle, messageSender1);
        playerConnector.connect(fixture.member2authName, fixture.gameHandle, messageSender2);
        GamePlay gamePlay = new GamePlay(fixture.gameStore, mockBroadcaster);
        gamePlay.start(fixture.gameHandle);

        forTrackingPlayerMessageSenders.remove(messageSender1);

        MessageSender reconnectedMessageSender1 = new DummyMessageSender();
        mockBroadcaster.reset();
        playerConnector.connect(fixture.member1authName, fixture.gameHandle, reconnectedMessageSender1);

        mockBroadcaster.verify();
    }

    private static class MockForInitialConnectionBroadcaster extends CrashTestDummyBroadcaster {
        private final Game expectedGame;
        private final Player expectedPlayer;
        private boolean announcePlayerConnectedToGame;
        private Game actualGame;
        private Player actualPlayer;

        public MockForInitialConnectionBroadcaster(Game expectedGame, Player expectedPlayer) {
            this.expectedGame = expectedGame;
            this.expectedPlayer = expectedPlayer;
        }

        public void verify() {
            assertThat(announcePlayerConnectedToGame)
                    .as("announcePlayerConnectedToGame() was not called after INITIAL Connection")
                    .isTrue();
            assertThat(actualGame)
                    .isEqualTo(expectedGame);
            assertThat(actualPlayer)
                    .isEqualTo(expectedPlayer);
        }

        @Override
        public void announcePlayerConnectedToGame(Game game, Player player) {
            announcePlayerConnectedToGame = true;
            this.actualGame = game;
            this.actualPlayer = player;
        }

        @Override
        public void prepareForGamePlay(Game game) {
            throw new IllegalStateException("prepareForGamePlay() should never be called during INITIAL Player Connection(s)");
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
        public void remove(MessageSender messageSender) {}

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

    private static class MockReconnectBroadcaster implements Broadcaster {
        private boolean gameUpdateCalled;
        private boolean prepareForGamePlayCalled;
        private boolean announcePlayerConnectedToGame;

        @Override
        public void announcePlayerConnectedToGame(Game game, Player player) {
            announcePlayerConnectedToGame = true;
        }

        @Override
        public void prepareForGamePlay(Game game) {
            prepareForGamePlayCalled = true;
        }

        @Override
        public void gameUpdate(Game game) {
            gameUpdateCalled = true;
        }

        void verify() {
            assertThat(announcePlayerConnectedToGame)
                    .as("Upon RECONNECT, announcePlayerConnectedToGame() should not have been called")
                    .isFalse();
            assertThat(prepareForGamePlayCalled)
                    .as("prepareForGamePlay() should have been called after Player RECONNECTED, but was not")
                    .isTrue();
            assertThat(gameUpdateCalled)
                    .as("gameUpdate() should have been called after Player RECONNECTED, but was not")
                    .isTrue();
        }

        public void reset() {
            announcePlayerConnectedToGame = false;
            prepareForGamePlayCalled = false;
            gameUpdateCalled = false;
        }
    }
}