package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public Broadcaster dummyBroadcaster() {
        return new Broadcaster() {
            @Override
            public void announcePlayerConnectedToGame(Game game, Player player) {
            }

            @Override
            public void clearStartGameModal(Game game) {
            }
        };
    }
}
