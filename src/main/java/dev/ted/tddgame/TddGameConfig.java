package dev.ted.tddgame;

import dev.ted.tddgame.application.GameCreator;
import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.application.port.GameStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TddGameConfig {

    @Bean
    public GameStore gameStore() {
        return new GameStore();
    }

    @Bean
    public GameCreator gameCreator(GameStore gameStore) {
        return GameCreator.create(gameStore);
    }

    @Bean
    public PlayerJoinsGame playerJoinsGame() {
        return PlayerJoinsGame.createNull();
    }

}
