package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class WebSocketBroadcasterTest {

    @Test
    void htmlSentToAllConnectedPlayersUponPlayerConnected() {
        Fixture fixture = createGameWithTwoPlayersConnectedHavingOneUniqueCard();
        fixture.game.start();

        fixture.broadcaster.announcePlayerConnectedToGame(fixture.game,
                                                          fixture.oliverPlayer);

        // expect HTML sent to all connected sessions
        assertThat(fixture.messageSenderForOliver.lastSentMessage())
                .as("Last sent message should not be null, i.e., was never called")
                .isNotNull()
                .isNotEmpty();
        assertThat(fixture.messageSenderForSamantha.lastSentMessage())
                .as("Last sent message should not be null, i.e., was never called")
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    void playerSpecificHtmlSentUponGameUpdate() {
        Fixture fixture = createGameWithTwoPlayersConnectedHavingOneUniqueCard();

        fixture.broadcaster.gameUpdate(fixture.game);

        assertThat(fixture.messageSenderForOliver.lastSentMessage())
                .as("Oliver's HTML should not be the same as Samantha's (as they have different cards in their hands)")
                .isNotEqualTo(fixture.messageSenderForSamantha.lastSentMessage());
    }

    // FIXTURE setup

    private Fixture createGameWithTwoPlayersConnectedHavingOneUniqueCard() {
        Game game = Game.create("irrelevant game name", "gameHandle");
        MemberId memberIdForOliver = new MemberId(78L);
        game.join(memberIdForOliver, "Oliver");
        MemberId memberIdForSamantha = new MemberId(63L);
        game.join(memberIdForSamantha, "Samantha");

        Player oliverPlayer = game.playerFor(memberIdForOliver);
        oliverPlayer.apply(new PlayerDrewActionCard(memberIdForOliver,
                                                    ActionCard.PREDICT));

        Player samanthaPlayer = game.playerFor(memberIdForSamantha);
        samanthaPlayer.apply(new PlayerDrewActionCard(memberIdForSamantha,
                                                      ActionCard.CODE_BLOAT));

        MessageSendersForPlayers messageSendersForPlayers = new MessageSendersForPlayers();
        MessageSenderSpy messageSenderForOliver = new MessageSenderSpy();
        messageSendersForPlayers.add(messageSenderForOliver, "gameHandle", oliverPlayer.id());
        MessageSenderSpy messageSenderForSamantha = new MessageSenderSpy();
        messageSendersForPlayers.add(messageSenderForSamantha, "gameHandle", samanthaPlayer.id());
        WebSocketBroadcaster broadcaster = new WebSocketBroadcaster(messageSendersForPlayers);
        return new Fixture(game, memberIdForOliver, messageSenderForOliver, oliverPlayer, messageSenderForSamantha, broadcaster);
    }

    private record Fixture(Game game,
                           MemberId memberIdForOliver,
                           MessageSenderSpy messageSenderForOliver,
                           Player oliverPlayer,
                           MessageSenderSpy messageSenderForSamantha,
                           WebSocketBroadcaster broadcaster) {
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