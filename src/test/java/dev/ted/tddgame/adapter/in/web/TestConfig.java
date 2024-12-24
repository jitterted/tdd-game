package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.adapter.out.websocket.MessageSendersForPlayers;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.port.Broadcaster;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(MessageSendersForPlayers.class)
public class TestConfig {

    @Bean
    public Broadcaster dummyBroadcaster() {
        return new GamePlayTest.NoOpDummyBroadcaster();
    }
}
