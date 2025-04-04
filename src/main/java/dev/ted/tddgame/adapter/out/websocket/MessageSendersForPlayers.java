package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.port.ForTrackingPlayerMessageSenders;
import dev.ted.tddgame.domain.PlayerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageSendersForPlayers implements ForTrackingPlayerMessageSenders {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendersForPlayers.class);

    private final Multimap<String, MessageSender> gameHandleToMessageSender = new Multimap<>();
    private final Map<GamePlayerCompositeKey, MessageSender> gamePlayerToMessageSender = new ConcurrentHashMap<>();

    @Override
    public void add(MessageSender messageSender, String gameHandle, PlayerId playerId) {
        gamePlayerToMessageSender.put(new GamePlayerCompositeKey(gameHandle, playerId), messageSender);
        gameHandleToMessageSender.put(gameHandle, messageSender);
    }

    @Override
    public void remove(MessageSender messageSender) {
        gameHandleToMessageSender.removeValue(messageSender);
        gamePlayerToMessageSender.values().remove(messageSender);
    }

    public void sendToAll(String gameHandle, String html) {
        gameHandleToMessageSender
                .get(gameHandle)
                .forEach(messageSender -> messageSender.sendMessage(html));
    }

    public void sendTo(String gameHandle, PlayerId playerId, String message) {
        GamePlayerCompositeKey key = new GamePlayerCompositeKey(gameHandle, playerId);
        if (gamePlayerToMessageSender.containsKey(key)) {
            gamePlayerToMessageSender
                    .get(key)
                    .sendMessage(message);
        } else {
            LOGGER.warn("Attempted to send message directly to unknown Player '{}' (disconnected?) in Game '{}'", playerId, gameHandle);
        }
    }

    static class Multimap<K, V> {
        private final Map<K, Set<V>> map = new ConcurrentHashMap<>();

        public void put(K key, V value) {
            Set<V> values = map.computeIfAbsent(key, _ -> new HashSet<>());
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

        public void removeValue(V value) {
            map.values().forEach(set -> set.remove(value));
        }
    }

    private record GamePlayerCompositeKey(String gameHandle, PlayerId playerId) {
    }
}

