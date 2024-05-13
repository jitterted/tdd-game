package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void newGameAssignedIdentifier() {
        Game game = new Game("name", "windy-dolphin-68");

        assertThat(game.handle())
                .isNotNull()
                .isNotBlank();
    }

    @Nested
    class CommandsGenerateEvents {

        @Test
        void creatingGameEmitsGameCreatedEvent() {
            Game game = Game.create("game name", "lovely-dog-23");

            Stream<GameEvent> events = game.freshEvents();

            assertThat(events)
                    .containsExactly(new GameCreated("game name", "lovely-dog-23"));
        }

        @Test
        void playerJoiningEmitsPlayerJoinedEvent() {
            Game game = createFreshGame();

            game.join(new PersonId(88L));

            assertThat(game.freshEvents())
                    .containsExactly(new PlayerJoined(new PersonId(88L)));
        }

        private static Game createFreshGame() {
            return Game.reconstitute(List.of(new GameCreated("IRRELEVANT NAME", "IRRELEVANT HANDLE")));
        }
    }

    @Nested
    class EventsProjectState {

        @Test
        void newGameHasGameNameAndHandle() {
            List<GameEvent> events = List.of(
                    new GameCreated("jitterted", "breezy-cat-85"));
            Game game = Game.reconstitute(events);

            assertThat(game.name())
                    .isEqualTo("jitterted");
            assertThat(game.handle())
                    .isEqualTo("breezy-cat-85");
        }

        @Test
        void playerJoinedResultsInPlayerAddedToGame() {
            List<GameEvent> events = gameWithFreshEvents(
                    new PlayerJoined(new PersonId(53L)));

            Game game = Game.reconstitute(events);

            assertThat(game.players())
                    .hasSize(1)
                    .extracting(Player::personId)
                    .containsExactly(new PersonId(53L));
        }

        private static List<GameEvent> gameWithFreshEvents(PlayerJoined freshEvent) {
            return List.of(new GameCreated("jitterted", "breezy-cat-85"),
                           freshEvent);
        }
    }

}