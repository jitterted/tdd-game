package dev.ted.tddgame.application;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.PersonId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class PlayerJoinsGameTest {

    @Test
    void newGameIsNotFull() {
        Game game = new Game("new", "new");

        assertThat(game.isFull())
                .isFalse();
    }

    @Test
    void personJoinsExistingGameThenAddedAsPlayer() {
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");
        Person person = new Person(new PersonId(27L));

        Player player = playerJoinsGame.join(person, game);

        assertThat(player.id())
                .isNotNull();
        assertThat(player.personId().id())
                .isEqualTo(27L);

        assertThat(game.players())
                .hasSize(1)
                .extracting(Player::personId)
                .containsExactly(new PersonId(27L));
    }

    @Test
    void twoPersonsJoinExistingGameThenBothAddedAsDifferentPlayers() {
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");
        Person firstPerson = new Person(new PersonId(7L));
        Person secondPerson = new Person(new PersonId(8L));

        Player firstPlayer = playerJoinsGame.join(firstPerson, game);
        Player secondPlayer = playerJoinsGame.join(secondPerson, game);

        assertThat(firstPlayer)
                .isNotEqualTo(secondPlayer);
    }

    @Test
    void personJoinsIsRejoinWhenAlreadyPlayerInGame() {
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");
        Person person = new Person(new PersonId(7L));
        Player player = playerJoinsGame.join(person, game);
        playerJoinsGame.join(new Person(new PersonId(8L)), game);

        Player joinAgainPlayer = playerJoinsGame.join(person, game);

        assertThat(joinAgainPlayer)
                .isEqualTo(player);
    }

    @Test
    void gameWith_4_PlayersIsFull() {
        Game game = gameWith4Players();

        assertThat(game.isFull())
                .isTrue();
    }

    private Game gameWith4Players() {
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame();
        Game game = new GameCreator().createNewGame("TDD Game");

        Stream.of(7L, 9L, 11L, 13L)
              .map(id -> new Person(new PersonId(id)))
              .forEach(person -> playerJoinsGame.join(person, game));

        return game;
    }
}