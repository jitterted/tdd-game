package dev.ted.tddgame.adapter.in.websocket;

import dev.ted.tddgame.adapter.out.websocket.WebSocketBroadcaster;
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

    // Map<WebSocketSession, List<GameHandle>>, i.e., a Multi-valued Map, e.g. https://github.com/eclipse/eclipse-collections/blob/master/docs/guide.md#-multimap

    private final PlayerConnector playerConnector;
    private final WebSocketBroadcaster webSocketBroadcaster;

    @Autowired
    public WebSocketInboundHandler(PlayerConnector playerConnector, WebSocketBroadcaster webSocketBroadcaster) {
        this.playerConnector = playerConnector;
        this.webSocketBroadcaster = webSocketBroadcaster;
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
        String gameHandle = messagePayload.split(":")[1];
        webSocketBroadcaster.connect(session, gameHandle);

        playerConnector.connect(session.getPrincipal().getName(), gameHandle);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        webSocketBroadcaster.disconnect(session);
    }

}