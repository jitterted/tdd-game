package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class WebSocketBroadcaster implements Broadcaster {
    private final MessageSendersForPlayers messageSendersForPlayers;

    public WebSocketBroadcaster(MessageSendersForPlayers messageSendersForPlayers) {
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
        String html = """
                      <swap id="modal-container" hx-swap-oob="delete" />
                      """;
        messageSendersForPlayers.sendToAll(game.handle(), html);
    }

    @Override
    public void gameUpdate(Game game) {
        // for each player, create HTML for that player
        for (Player player : game.players()) {
            String playerHtml = player.playerName(); // generate customized HTML for this player
            messageSendersForPlayers.sendTo(game.handle(), player.id(), playerHtml);
        }
    }

}
