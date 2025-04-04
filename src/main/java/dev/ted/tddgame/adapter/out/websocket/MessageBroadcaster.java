package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class MessageBroadcaster implements Broadcaster {
    private final MessageSendersForPlayers messageSendersForPlayers;

    public MessageBroadcaster(MessageSendersForPlayers messageSendersForPlayers) {
        this.messageSendersForPlayers = messageSendersForPlayers;
    }

    @Override
    public void announcePlayerConnectedToGame(Game game, Player player) {
        String html =
                WaitingRoomHtmlRenderer.forConnectNotification(LocalTime.now(), player.playerName())
                + WaitingRoomHtmlRenderer.forJoinedPlayers(game.players());
        // create PlayerViewComponent(player, sessionForThatPlayer)
        messageSendersForPlayers.sendToAll(game.handle(), html);
    }

    @Override
    public void prepareForGamePlay(Game game) {
        sendHtmlToRemoveModalContainerForEveryone(game);
        sendHtmlForOtherPlayerPlaceholderContainers(game);
    }

    @Override
    public void gameUpdate(Game game) {
        sendYourHtmlForEachPlayerOf(game);
        sendOtherPlayerHandsToAll(game);
        sendActionCardDeckToAll(game);
        sendTestResultsDeckToAll(game);
        sendWorkspacePawnsToAll(game);

        sendYourWorkspaceInPlayCardsForEachPlayerOf(game);
        // send other players' Workspace in-play cards to all

        // send Test Results deck update
        // send commit & risk tracking updates

        // show Test Results card drawn as Modal (if any workspace has one)

    }

    private void sendYourWorkspaceInPlayCardsForEachPlayerOf(Game game) {
        for (Player player : game.players()) {
            messageSendersForPlayers.sendTo(
                    game.handle(),
                    player.id(),
                    new WorkspaceViewComponent(game.players())
                            .htmlForInPlayCardsForYou(player.workspace())
                            .render());
            messageSendersForPlayers.sendTo(
                    game.handle(),
                    player.id(),
                    new WorkspaceViewComponent(game.players())
                            .htmlForTechNeglectCardsForYou(player.workspace())
                            .render());
        }
    }

    private void sendWorkspacePawnsToAll(Game game) {
        messageSendersForPlayers.sendToAll(
                game.handle(),
                new WorkspaceViewComponent(game.players())
                        .htmlForPawns().render()
        );
    }

    private void sendActionCardDeckToAll(Game game) {
        messageSendersForPlayers.sendToAll(
                game.handle(),
                DeckViewComponent.forActionCardDeck(game.actionCardDeck())
                                 .htmlForDiscardAndDrawPiles().render());
    }

    private void sendTestResultsDeckToAll(Game game) {
        messageSendersForPlayers.sendToAll(
                game.handle(),
                DeckViewComponent.forTestResultsCardDeck(game.testResultsCardDeck())
                                 .htmlForDiscardAndDrawPiles().render());
    }

    private void sendOtherPlayerHandsToAll(Game game) {
        messageSendersForPlayers.sendToAll(
                game.handle(),
                new OtherPlayersViewComponent(game)
                        .htmlForOtherPlayers().render());
    }

    private void sendHtmlToRemoveModalContainerForEveryone(Game game) {
        String html = HtmlElement.swapDelete("modal-container").render();
        messageSendersForPlayers.sendToAll(game.handle(), html);
    }

    private void sendHtmlForOtherPlayerPlaceholderContainers(Game game) {
        for (Player player : game.players()) {
            HtmlElement htmlElement = new PlayerViewComponent(game.handle(), player)
                    .htmlPlaceholdersForOtherPlayers(game.players());
            messageSendersForPlayers.sendTo(game.handle(),
                                            player.id(),
                                            htmlElement.render());
        }
    }

    private void sendYourHtmlForEachPlayerOf(Game game) {
        // FUTURE: we want to iterate through all connected View Components (that encapsulated the MessageSender)
        // so that way we can broadcast updates to Observers who are NOT Players in the game
        for (Player player : game.players()) {
            HtmlElement htmlElement = new PlayerViewComponent(game.handle(), player)
                    .htmlForYou();
            messageSendersForPlayers.sendTo(
                    game.handle(),
                    player.id(),
                    htmlElement.render());
        }
    }

}
