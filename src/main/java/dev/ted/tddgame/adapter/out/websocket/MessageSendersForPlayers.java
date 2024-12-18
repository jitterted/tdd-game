package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.port.ForTrackingPlayerMessageSenders;
import dev.ted.tddgame.domain.PlayerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageSendersForPlayers implements ForTrackingPlayerMessageSenders {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendersForPlayers.class);

    private final Multimap<String, MessageSender> gameHandleToMessageSender = new Multimap<>();
    private final Map<GamePlayerCompositeKey, MessageSender> gamePlayerToMessageSender = new ConcurrentHashMap<>();

    public MessageSendersForPlayers() {
    }

    @Deprecated
    public void connect(MessageSender messageSender, String gameHandle) {
        gameHandleToMessageSender.put(gameHandle, messageSender);
    }

    @Override
    public void add(MessageSender messageSender, String gameHandle, PlayerId playerId) {
        gamePlayerToMessageSender.put(new GamePlayerCompositeKey(gameHandle, playerId), messageSender);
        gameHandleToMessageSender.put(gameHandle, messageSender);
    }

    /**
     * this needs to look up the session in both maps and remove them from each
     */
    @Override
    public void remove(WebSocketSession webSocketSession) {
        // Remove this WebSocketSession from both Maps

        // tell each Game that the player has disconnected:
        // by using a SessionToGameMap

    }

    public void sendToAll(String gameHandle, String html) {
        gameHandleToMessageSender
                .get(gameHandle)
                .forEach(messageSender -> messageSender.sendMessage(html));
    }

    public void sendTo(String gameHandle, PlayerId playerId, String message) {
        gamePlayerToMessageSender
                .get(new GamePlayerCompositeKey(gameHandle, playerId))
                .sendMessage(message);
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

    private record GamePlayerCompositeKey(String gameHandle, PlayerId playerId) {
    }
}

