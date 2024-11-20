package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.PlayerConnections;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Set;

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
        sendHtmlTo(playerConnections.getGameHandleToSessions()
                                    .get(game.handle()), html);
    }

    @Override
    public void clearStartGameModal(Game game) {
        String html = """
                      <swap id="modal-container" hx-swap-oob="delete" />
                      """;
        sendHtmlTo(playerConnections.getGameHandleToSessions()
                                    .get(game.handle()), html);
    }

    @Override
    public void gameUpdate(Game game) {
        // customize the HTML transformation for each player
        throw new UnsupportedOperationException("Game Update not implemented yet");
    }

    public void sendHtmlTo(Set<WebSocketSession> webSocketSessions, String html) {
        webSocketSessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(html));
                }
                // else remove them from the maps
            } catch (IOException e) {
                LOGGER.warn("Unable to send message to session: " + session.getId(), e);
            }
        });
    }

}
