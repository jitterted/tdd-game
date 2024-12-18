package dev.ted.tddgame.application.port;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.domain.PlayerId;
import org.springframework.web.socket.WebSocketSession;

public interface ForTrackingPlayerMessageSenders {
    void add(MessageSender messageSender, String gameHandle, PlayerId playerId);

    void remove(WebSocketSession webSocketSession);
}
