package dev.ted.tddgame;

import dev.ted.tddgame.adapter.in.websocket.WebSocketInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebSocketInboundHandler webSocketHandler;

    @Autowired
    public WebSocketConfiguration(WebSocketInboundHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/game");
        // DON'T DO THIS! Uses the same endpoint as the MVC handler
//        registry.addHandler(webSocketHandler, "/game/ws");
    }

}
