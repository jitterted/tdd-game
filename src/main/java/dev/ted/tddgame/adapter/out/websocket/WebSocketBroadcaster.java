package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.PlayerConnections;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class WebSocketBroadcaster implements Broadcaster {
    private final PlayerConnections playerConnections;

    public WebSocketBroadcaster(PlayerConnections playerConnections) {
        this.playerConnections = playerConnections;
    }

    @Override
    public void announcePlayerConnectedToGame(Game game, Player player) {
        String html =
                WaitingRoomHtmlRenderer.forConnectNotification(LocalTime.now(), player.playerName())
                + WaitingRoomHtmlRenderer.forJoinedPlayers(game.players());
        // create PlayerViewComponent(player, sessionForThatPlayer)
        playerConnections.sendToAll(game, html);
    }

    @Override
    public void clearStartGameModal(Game game) {
        String html = """
                      <swap id="modal-container" hx-swap-oob="delete" />
                      """;
        playerConnections.sendToAll(game, html);
    }

    @Override
    public void gameUpdate(Game game) {
        // for each player, create HTML for that player
        for (Player player : game.players()) {
            String playerHtml = ""; // generate customized HTML for this player
//            playerConnections.sendToPlayer(game.handle(), player.id(), playerHtml);
        }

        throw new UnsupportedOperationException("Game Update not implemented yet");
    }

}
