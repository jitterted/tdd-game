package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.DeckView;
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
        // send Action Card deck update (sent as single "Forest" message):
        // 1. send back of card for draw pile
        // 2. send front of last discarded card for discard pile
        messageSendersForPlayers.sendToAll(game.handle(),
                                           createActionCardDeckHtmlElement(game.actionCardDeck())
                                                      .render());

        // send Test Results deck update
        // send workspace update
        // send commit & risk tracking updates
    }

    private HtmlElement createActionCardDeckHtmlElement(DeckView<ActionCard> actionCardDeckView) {
        return HtmlElement.forest(actionCardDrawPile(),
                                  actionCardDiscardPile(actionCardDeckView));
    }

    private HtmlElement actionCardDiscardPile(DeckView<ActionCard> actionCardDeckView) {
        ActionCard topCardOnDiscardPile = actionCardDeckView.discardPile().getLast();
        return HtmlElement.swapInnerHtml(
                "action-card-discard-pile",
                HandViewComponent.imgElementFor(topCardOnDiscardPile)
        );
    }

    private HtmlElement actionCardDrawPile() {
        return HtmlElement.swapInnerHtml(
                "action-card-draw-pile",
                HtmlElement.img("/action-card-back.png", "Action Card Draw Pile"));
    }

    private void sendOtherPlayerHandsToAll(Game game) {
        messageSendersForPlayers.sendToAll(game.handle(),
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
            HtmlElement htmlElement = new PlayerViewComponent(game.handle(), player).htmlForYou();
            messageSendersForPlayers.sendTo(
                    game.handle(),
                    player.id(),
                    htmlElement.render());
        }
    }

}
