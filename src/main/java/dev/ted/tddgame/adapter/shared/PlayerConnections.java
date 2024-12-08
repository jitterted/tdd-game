package dev.ted.tddgame.adapter.shared;

import dev.ted.tddgame.domain.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerConnections {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerConnections.class);

    private final Multimap<String, MessageSender> gameHandleToMessageSender = new Multimap<>();

    public PlayerConnections() {
    }

    public void connect(MessageSender messageSender, String gameHandle) {
        gameHandleToMessageSender.put(gameHandle, messageSender);
    }

    public void disconnect(WebSocketSession webSocketSession) {
        // Remove this WebSocketSession from all Game handles

        // tell each Game that the player has disconnected:
        // by using a SessionToGameMap

    }

    public void sendToAll(Game game, String html) {
        gameHandleToMessageSender
                .get(game.handle())
                .forEach(messageSender -> messageSender.sendMessage(html));
    }

    private static class Multimap<K, V> {
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

}

