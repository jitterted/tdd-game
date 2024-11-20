package dev.ted.tddgame.adapter.in.websocket;

import dev.ted.tddgame.adapter.shared.PlayerConnections;
import dev.ted.tddgame.application.PlayerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketInboundHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketInboundHandler.class);

    private final PlayerConnector playerConnector;
    private final PlayerConnections playerConnections;

    @Autowired
    public WebSocketInboundHandler(PlayerConnector playerConnector, PlayerConnections playerConnections) {
        this.playerConnector = playerConnector;
        this.playerConnections = playerConnections;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Websocket connection established, session ID: {}, session remote address: {}", session.getId(), session.getRemoteAddress());
        // can't tell the PlayerConnector that a new player has connected here, because we don't know which game they want
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        LOGGER.info("WebSocket Text Message received from session: {}, with message: {}", session.toString(), message.toString());
        String messagePayload = (String) message.getPayload();
        LOGGER.info("Payload details: {}", messagePayload);
        String gameHandle = messagePayload.split(":")[1]; // e.g. "join:sleepy-goose-78"
        playerConnections.connect(session, gameHandle);

        String playerUsername = session.getPrincipal().getName();
        playerConnector.connect(playerUsername, gameHandle);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        playerConnections.disconnect(session);
    }

}