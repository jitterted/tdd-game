package dev.ted.tddgame.adapter.in.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketInboundHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketInboundHandler.class);

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    // Map<WebSocketSession, List<GameHandle>>, i.e., a Multi-valued Map, e.g. https://github.com/eclipse/eclipse-collections/blob/master/docs/guide.md#-multimap

    // DEPENDS ON: PlayerConnector

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Websocket connection established, session ID: {}, session remote address: {}", session.getId(), session.getRemoteAddress());
        sessionMap.put(session.getId(), session);
        // can't tell the PlayerConnector that a new player has connected here, because we don't know which game they want
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        LOGGER.info("WebSocket Text Message received from session: {}, with message: {}", session.toString(), message.toString());
        LOGGER.info("Payload details: {}", message.getPayload());
        // if we get a "connect:gameHandle" message, add WebSocketSession to GameHandles map entry
        // |-> and tell PlayerConnector that a new player has connected: game, player username (from the Principal)
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionMap.remove(session.getId());
        // tell Game that the player has disconnected: ?? How do I know which Game they were connected to ??
        // answer: use the SessionToGameMap
    }

}