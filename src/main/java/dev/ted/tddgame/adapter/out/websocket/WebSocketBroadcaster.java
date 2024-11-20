package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.PlayerConnections;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class WebSocketBroadcaster implements Broadcaster {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketBroadcaster.class);
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
        playerConnections.send(game, html);
    }

    @Override
    public void clearStartGameModal(Game game) {
        String html = """
                      <swap id="modal-container" hx-swap-oob="delete" />
                      """;
        playerConnections.send(game, html);
    }

    @Override
    public void gameUpdate(Game game) {
        // customize the HTML transformation for each player
        throw new UnsupportedOperationException("Game Update not implemented yet");
    }

}
