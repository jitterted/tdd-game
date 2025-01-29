package dev.ted.tddgame;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TddGameStartup {

    public static void main(String[] args) {
        SpringApplication.run(TddGameStartup.class, args);
    }

    @Bean
    public CommandLineRunner addGameToGameStore(GameStore gameStore, MemberStore memberStore) {
        return _ -> {
            Game game = new Game.GameFactory().create("Preloaded Test Game", "testy-spider-83");
            gameStore.save(game);
            Member member = new Member(new MemberId(21L), "Red", "Red");
            memberStore.save(member);
            game.join(member.id(), "Mr. Red");
        };
    }

}
