package dev.ted.tddgame;

import dev.ted.tddgame.adapter.out.websocket.MessageSendersForPlayers;
import dev.ted.tddgame.application.GameCreator;
import dev.ted.tddgame.application.GameFinder;
import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.MemberFinder;
import dev.ted.tddgame.application.PlayerConnector;
import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TddGameConfig {

    @Bean
    public GameStore gameStore() {
        return GameStore.createEmpty();
    }

    @Bean
    public MemberStore memberStore() {
        return new MemberStore();
    }

    @Bean
    public GameCreator gameCreator(GameStore gameStore) {
        return GameCreator.create(gameStore);
    }

    @Bean
    public PlayerJoinsGame playerJoinsGame(GameStore gameStore) {
        return new PlayerJoinsGame(gameStore);
    }

    @Bean
    public PlayerConnector playerConnector(Broadcaster broadcaster,
                                           MemberStore memberStore,
                                           GameStore gameStore,
                                           MessageSendersForPlayers messageSendersForPlayers) {
        return new PlayerConnector(broadcaster,
                                   new MemberFinder(memberStore),
                                   new GameFinder(gameStore),
                                   messageSendersForPlayers);
    }

    @Bean
    public GamePlay gamePlay(GameStore gameStore, Broadcaster broadcaster) {
        return new GamePlay(gameStore, broadcaster);
    }

}
