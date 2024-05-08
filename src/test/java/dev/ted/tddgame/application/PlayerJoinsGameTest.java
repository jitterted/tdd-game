package dev.ted.tddgame.application;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerJoinsGameTest {

    @Test
    void personJoinsExistingGameThenAddedAsPlayerIfNotAlreadyJoined() {
        Person person = new Person(27L);
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");

        Player player = playerJoinsGame.join(person, game);

        assertThat(player.id())
                .isNotNull();

        assertThat(game.players())
                .containsExactly(new Player(1L, 27L));

        assertThat(player.personId())
                .isEqualTo(27L);
    }
}