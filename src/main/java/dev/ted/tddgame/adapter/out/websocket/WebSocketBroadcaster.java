package dev.ted.tddgame.adapter.out.websocket;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketBroadcaster implements Broadcaster {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketBroadcaster.class);
    private final Multimap<String, WebSocketSession> gameHandleToSession = new Multimap<>();

    @Override
    public void announcePlayerConnectedToGame(Game game, Player player) {
        String html =
                WaitingRoomHtmlRenderer.forConnectNotification(LocalTime.now(), player.playerName())
                + WaitingRoomHtmlRenderer.forJoinedPlayers(game.players());
        sendHtmlTo(gameHandleToSession.get(game.handle()), html);
    }

    @Override
    public void clearStartGameModal(Game game) {
        String html = """
                      <swap id="modal-container" hx-swap-oob="delete" />
                      """;
        sendHtmlTo(gameHandleToSession.get(game.handle()), html);
    }

    @Override
    public void gameUpdate(Game game) {
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

    public void disconnect(WebSocketSession webSocketSession) {
        // Remove this WebSocketSession from all Game handles

        // tell each Game that the player has disconnected:
        // by using a SessionToGameMap

    }

    public void connect(WebSocketSession session, String gameHandle) {
        gameHandleToSession.put(gameHandle, session);
    }
}

class Multimap<K, V> {
    private final Map<K, Set<V>> map = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        Set<V> values = map.computeIfAbsent(key, k -> new HashSet<>());
        values.add(value);
    }

    public Set<V> get(K key) {
        return map.getOrDefault(key, new HashSet<>());
    }

    public boolean remove(K key, V value) {
        Set<V> values = map.get(key);
        if (values != null) {
            boolean removed = values.remove(value);
            if (values.isEmpty()) {
                map.remove(key);
            }
            return removed;
        }
        return false;
    }
}