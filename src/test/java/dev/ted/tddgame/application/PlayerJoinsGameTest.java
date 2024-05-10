package dev.ted.tddgame.application;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerJoinsGameTest {

    @Test
    void personJoinsExistingGameThenAddedAsPlayer() {
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");
        Person person = new Person(27L);

        Player player = playerJoinsGame.join(person, game);

        assertThat(player.id())
                .isNotNull();
        assertThat(player.personId())
                .isEqualTo(27L);

        assertThat(game.players())
                .hasSize(1)
                .extracting(Player::personId)
                .containsExactly(27L);
    }

    @Test
    void twoPersonsJoinExistingGameThenBothAddedAsDifferentPlayers() {
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");
        Person firstPerson = new Person(7L);
        Person secondPerson = new Person(8L);

        Player firstPlayer = playerJoinsGame.join(firstPerson, game);
        Player secondPlayer = playerJoinsGame.join(secondPerson, game);

        assertThat(firstPlayer)
                .isNotEqualTo(secondPlayer);
    }

    @Test
    void personJoinsIsRejoinWhenAlreadyPlayerInGame() {
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");
        Person person = new Person(7L);
        Player player = playerJoinsGame.join(person, game);
        playerJoinsGame.join(new Person(8L), game);

        Player joinAgainPlayer = playerJoinsGame.join(person, game);

        assertThat(joinAgainPlayer)
                .isEqualTo(player);
    }
}