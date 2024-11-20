package dev.ted.tddgame.adapter.shared;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class PlayerConnections {
    private final Multimap<String, WebSocketSession> gameHandleToSessions = new Multimap<String, WebSocketSession>();

    public PlayerConnections() {
    }

    public Multimap<String, WebSocketSession> getGameHandleToSessions() {
        return gameHandleToSessions;
    }

    public void connect(WebSocketSession session, String gameHandle) {
        getGameHandleToSessions()
                         .put(gameHandle, session);
    }

    public void disconnect(WebSocketSession webSocketSession) {
        // Remove this WebSocketSession from all Game handles

        // tell each Game that the player has disconnected:
        // by using a SessionToGameMap

    }
}

