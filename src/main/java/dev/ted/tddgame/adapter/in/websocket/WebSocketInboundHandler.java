package dev.ted.tddgame.adapter.in.websocket;

import dev.ted.tddgame.adapter.out.websocket.MessageSendersForPlayers;
import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.PlayerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;

@Component
public class WebSocketInboundHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketInboundHandler.class);

    private final PlayerConnector playerConnector;
    private final MessageSendersForPlayers messageSendersForPlayers;

    @Autowired
    public WebSocketInboundHandler(PlayerConnector playerConnector,
                                   MessageSendersForPlayers messageSendersForPlayers) {
        this.playerConnector = playerConnector;
        this.messageSendersForPlayers = messageSendersForPlayers;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Websocket connection established, session ID: {}, session remote address: {}, Principal: {}", session.getId(), session.getRemoteAddress(), session.getPrincipal());
        // can't tell the PlayerConnector that a new player has connected here, because we don't know which game they want
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        LOGGER.info("WebSocket Text Message received from session: {}, Principal: {}, with message: {}", session.toString(), session.getPrincipal(), message.toString());
        String messagePayload = (String) message.getPayload();
        LOGGER.info("Payload details: {}", messagePayload);
        String gameHandle = messagePayload.split(":")[1]; // e.g. "join:sleepy-goose-78"

        WebSocketMessageSender messageSender = new WebSocketMessageSender(session);

        // this is the username for the logged-in user, and NOT the name of the player in the context of the game
        String memberUsername = session.getPrincipal().getName();
        // need to look up or otherwise figure out the Player ID for this Member that is a Player in the Game
        playerConnector.connect(memberUsername, gameHandle, messageSender);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        LOGGER.info("Websocket connection closed for Member '{}', session ID: {}", session.getPrincipal(), session.getId());
        messageSendersForPlayers.remove(new WebSocketMessageSender(session));
    }

    private static class WebSocketMessageSender implements MessageSender {
        private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageSender.class);

        private final WebSocketSession webSocketSession;

        public WebSocketMessageSender(WebSocketSession webSocketSession) {
            Objects.requireNonNull(webSocketSession);
            this.webSocketSession = webSocketSession;
        }

        @Override
        public void sendMessage(String message) {
            try {
                if (webSocketSession.isOpen()) {
                    // throw exception so this connection gets removed from the map
                    webSocketSession.sendMessage(new TextMessage(message));
                } else {

                }
            } catch (IOException e) {
                LOGGER.warn("Unable to send message to webSocketSession: " + webSocketSession.getId(), e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            WebSocketMessageSender that = (WebSocketMessageSender) o;
            return webSocketSession.equals(that.webSocketSession);
        }

        @Override
        public int hashCode() {
            return webSocketSession.hashCode();
        }
    }
}