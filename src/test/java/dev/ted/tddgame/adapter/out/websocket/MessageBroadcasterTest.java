package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.ted.tddgame.adapter.HtmlElement.forest;
import static dev.ted.tddgame.adapter.HtmlElement.swapInnerHtml;
import static org.assertj.core.api.Assertions.*;

class MessageBroadcasterTest {

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
    void startGameSendsHtmlContainersForOtherPlayersThatAreNotYou() {
        Fixture fixture = createGameWithTwoPlayersConnectedHavingOneUniqueCard();

        fixture.broadcaster.prepareForGamePlay(fixture.game);

        // message text received for Samantha must not contain div with player-id-[samantha's ID]
        assertThat(fixture.messageSenderForSamantha.lastSentMessage())
                .contains("player-id-" + fixture.oliverPlayer.id().id())
                .doesNotContain("player-id-" + fixture.samanthaPlayer.id().id());
    }

    @Test
    @Disabled("Need to have a real game that is started so deck(s) are created")
    void gameUpdateSendsPlayerSpecificHtml() {
        Fixture fixture = createGameWithTwoPlayersConnectedHavingOneUniqueCard();

        fixture.broadcaster.gameUpdate(fixture.game);

        assertThat(fixture.messageSenderForOliver.sentMessages.size())
                .as("Should have 3 messages sent to Oliver")
                .isEqualTo(3);

        assertThat(fixture.messageSenderForSamantha.sentMessages.size())
                .as("Should have 3 messages sent to Samantha")
                .isEqualTo(3);

        assertThat(fixture.messageSenderForOliver.firstSentMessage())
                .as("Oliver's custom HTML should not be the same as Samantha's (as they have different cards in their hands)")
                .isNotEqualTo(fixture.messageSenderForSamantha.firstSentMessage());

        assertThat(fixture.messageSenderForOliver.lastSentMessage())
                .as("Oliver's HTML for 'other players' should be the same for everyone")
                .isEqualTo(fixture.messageSenderForSamantha.lastSentMessage());
    }

    @Test
    @Disabled("Until the DeckViewComponent exists and is tested for empty/non-empty piles")
    void gameUpdateSendsActionCardDeckHtml() {
        MemberId memberId = new MemberId(17L);
        Member member = new Member(memberId, "Blue (member name)", "blue-username");
        String gameHandle = "chatty-piggy-81";
        Game game = Game.create("Test Game", gameHandle);
        game.join(member.id(), "Blue (player name)");
        GameStore gameStore = GameStore.createEmpty();
        gameStore.save(game);

        MessageSendersForPlayers messageSendersForPlayers = new MessageSendersForPlayers();
        MessageSenderSpy messageSenderForBlue = new MessageSenderSpy();
        messageSendersForPlayers.add(messageSenderForBlue, gameHandle, game.playerFor(member.id()).id());
        MessageBroadcaster broadcaster = new MessageBroadcaster(messageSendersForPlayers);

        GamePlay gamePlay = new GamePlay(gameStore, broadcaster);
        gamePlay.start(gameHandle);

        game = gameStore.findByHandle(gameHandle).orElseThrow(); // get the latest game
        ActionCard cardToDiscard = game.playerFor(member.id()).hand().findFirst().get();
        gamePlay.discard(gameHandle, member.id(), cardToDiscard);

        // now the action card deck has the discarded card in the Discard Pile
        assertThat(messageSenderForBlue.lastSentMessage())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "action-card-draw-pile",
                                HtmlElement.img("/action-card-back.png",
                                                "Action Card Draw Pile")
                        ),
                        swapInnerHtml(
                                "action-card-discard-pile",
                                HandViewComponent.imgElementFor(cardToDiscard)
                        )
                ).render());
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
        public void sendMessage(String message) {
            sentMessages.add(message);
        }
    }
}