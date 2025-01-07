package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MessageBroadcasterTest {

    @Test
    // OBSOLETE: we want to check in a bit more detail what HTML was sent?
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
    void startGameSendsHtmlContainersForOtherPlayersThatAreNotYou() {
        Fixture fixture = createGameWithTwoPlayersConnectedHavingOneUniqueCard();

        fixture.broadcaster.prepareForGamePlay(fixture.game);

        // message text received for Samantha must not contain div with player-id-[samantha's ID]
        assertThat(fixture.messageSenderForSamantha.lastSentMessage())
                .contains("player-id-" + fixture.oliverPlayer.id().id())
                .doesNotContain("player-id-" + fixture.samanthaPlayer.id().id());
    }

    @Test
    void gameUpdateSendsPlayerSpecificHtml() {
        Fixture fixture = createGameWithTwoPlayersConnectedHavingOneUniqueCard();

        fixture.broadcaster.gameUpdate(fixture.game);

        assertThat(fixture.messageSenderForOliver.sentMessages.size())
                .as("Should have 2 messages sent to Oliver")
                .isEqualTo(2);

        assertThat(fixture.messageSenderForSamantha.sentMessages.size())
                .as("Should have 2 messages sent to Samantha")
                .isEqualTo(2);

        assertThat(fixture.messageSenderForOliver.firstSentMessage())
                .as("Oliver's custom HTML should not be the same as Samantha's (as they have different cards in their hands)")
                .isNotEqualTo(fixture.messageSenderForSamantha.firstSentMessage());

        assertThat(fixture.messageSenderForOliver.lastSentMessage())
                .as("Oliver's HTML for 'other players' should be the same for everyone")
                .isEqualTo(fixture.messageSenderForSamantha.lastSentMessage());
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
        MessageBroadcaster broadcaster = new MessageBroadcaster(messageSendersForPlayers);
        return new Fixture(game, memberIdForOliver, messageSenderForOliver, oliverPlayer, messageSenderForSamantha, samanthaPlayer, broadcaster);
    }

    private record Fixture(Game game,
                           MemberId memberIdForOliver,
                           MessageSenderSpy messageSenderForOliver,
                           Player oliverPlayer,
                           MessageSenderSpy messageSenderForSamantha,
                           Player samanthaPlayer,
                           MessageBroadcaster broadcaster) {
    }

    private static class MessageSenderSpy implements MessageSender {
        private final List<String> sentMessages = new ArrayList<>();

        String firstSentMessage() {
            return sentMessages.getFirst();
        }

        String lastSentMessage() {
            return sentMessages.getLast();
        }

        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public void sendMessage(String message) {
            sentMessages.add(message);
        }
    }
}