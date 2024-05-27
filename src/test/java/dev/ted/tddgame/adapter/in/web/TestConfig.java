package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.Broadcaster;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public Broadcaster dummyBroadcaster() {
        return (_, _) -> {};
    }
}
