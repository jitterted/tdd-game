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
            EventSourcedAggregate game = Game.create("game name", "lovely-dog-23");

            Stream<GameEvent> events = game.freshEvents();

            assertThat(events)
                    .containsExactly(new GameCreated("game name", "lovely-dog-23"));
        }

        @Test
        void name() {

        }
    }

    @Nested
    class EventsProjectState {

        @Test
        void newGameHasGameName() {
            List<GameEvent> events = List.of(new GameCreated("jitterted", "breezy-cat-85"));
            Game game = Game.reconstitute(events);

            assertThat(game.name())
                    .isEqualTo("jitterted");
            assertThat(game.handle())
                    .isEqualTo("breezy-cat-85");
        }
    }

}