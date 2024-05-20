package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.PersonId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class PlayerJoinsGameTest {

    @Test
    void personsCanJoinNewGame() {
        Game game = Game.create("new", "new");

        assertThat(game.canJoin(new PersonId(42L)))
                .isTrue();
    }

    @Test
    void personJoinsExistingGameThenAddedAsPlayer() {
        Fixture fixture = createFixture();
        Person person = new Person(new PersonId(27L));

        fixture.playerJoinsGame().join(person.id(), fixture.game().handle());

        assertThat(fixture.game().players())
                .hasSize(1)
                .extracting(Player::personId)
                .containsExactly(new PersonId(27L));
    }

    @Test
    void twoPersonsJoinExistingGameThenBothAddedAsDifferentPlayers() {
        Fixture fixture = createFixture();
        PersonId firstPerson = new PersonId(7L);
        PersonId secondPerson = new PersonId(8L);

        fixture.playerJoinsGame().join(firstPerson, fixture.game().handle());
        fixture.playerJoinsGame().join(secondPerson, fixture.game().handle());

        assertThat(fixture.game().players())
                .map(Player::personId)
                .containsExactly(firstPerson, secondPerson);
    }

    @Test
    void personJoinsIsNotAddedAsNewPlayerWhenAlreadyPlayerInGame() {
        Fixture fixture = createFixture();
        PersonId personId = new PersonId(7L);
        fixture.playerJoinsGame().join(personId, fixture.game().handle());
        fixture.playerJoinsGame().join(new PersonId(8L), fixture.game().handle());

        fixture.playerJoinsGame().join(personId, fixture.game().handle());

        assertThat(fixture.game().players())
                .as("Expected 2 players, as first person rejoined")
                .hasSize(2);
    }

    @Test
    void newPersonCanNotJoinGameWith_4_Players() {
        Game game = gameWith4Players(7L, 9L, 11L, 13L);

        assertThat(game.canJoin(new PersonId(42L)))
                .isFalse();
    }

    @Test
    void noExceptionThrownIfPlayerAlreadyInGame() {
        long existingPersonId = 11L;
        Game game = gameWith4Players(7L, 9L, existingPersonId, 13L);

        assertThatNoException()
                .isThrownBy(() -> game.join(new PersonId(existingPersonId)));
    }

    @Nested
    class UnhappyScenarios {

        @Test
        void exceptionThrownIfNewPlayerCanNotJoinGame() {
            Game game = gameWith4Players(7L, 9L, 11L, 13L);

            assertThatIllegalStateException()
                    .isThrownBy(() -> game.join(new PersonId(18L)))
                    .withMessage("Game is full (Person IDs: [7, 9, 11, 13]), so PersonId[id=18] cannot join.");
        }

        @Test
        void exceptionThrownIfGameHandleIsNotFoundInGameStore() {
            PlayerJoinsGame playerJoinsGame = PlayerJoinsGame.createNull();

            assertThatIllegalArgumentException()
                    .isThrownBy(() -> playerJoinsGame.join(new PersonId(42L), "non-existing-game-handle"))
                    .withMessage("Game with handle 'non-existing-game-handle' was not found in the GameStore.");
        }
    }
    // -- ENCAPSULATED SETUP

    private Game gameWith4Players(Long... personIds) {
        Fixture fixture = createFixture();

        Stream.of(personIds)
              .map(PersonId::new)
              .forEach(personId -> fixture.playerJoinsGame()
                                          .join(personId, fixture.game().handle()));

        return fixture.game();
    }

    private static Fixture createFixture() {
        GameStore gameStore = new GameStore();
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame(gameStore);
        Game game = new GameCreator(gameStore).createNewGame("TDD Game");
        return new Fixture(playerJoinsGame, game);
    }

    private record Fixture(PlayerJoinsGame playerJoinsGame, Game game) {
    }


}