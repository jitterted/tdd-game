package dev.ted.tddgame.adapter.out.websocket;

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
    public void clearStartGameModal(Game game) {
        sendHtmlToRemoveModalContainerForEveryone(game);
        sendHtmlForOtherPlayerPlaceholderContainers(game);
    }

    private void sendHtmlToRemoveModalContainerForEveryone(Game game) {
        String html = HtmlComponent.swapDelete("modal-container").render();
        messageSendersForPlayers.sendToAll(game.handle(), html);
    }

    private void sendHtmlForOtherPlayerPlaceholderContainers(Game game) {
        for (Player player : game.players()) {
            HtmlComponent htmlComponent = new PlayerViewComponent(player)
                    .htmlForOtherPlayers(game.players());
            messageSendersForPlayers.sendTo(game.handle(),
                                            player.id(),
                                            htmlComponent.render());
        }
    }

    @Override
    public void gameUpdate(Game game) {
        sendCustomHtmlForEachPlayerOf(game);
        // send "other player compact view" to all
    }

    private void sendCustomHtmlForEachPlayerOf(Game game) {
        // FUTURE: we want to iterate through all connected View Components (that encapsulated the MessageSender)
        // so that way we can broadcast updates to Observers who are NOT Players in the game
        for (Player player : game.players()) {
            messageSendersForPlayers.sendTo(
                    game.handle(),
                    player.id(),
                    new PlayerViewComponent(player).generateHtmlAsYou());
        }
    }

}
