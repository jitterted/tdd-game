package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.GameCreated;
import dev.ted.tddgame.domain.GameEvent;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.PlayerJoined;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class EventDtoTest {

    @Test
    void givenPlayerWonGameEventCreateEventDto() {
        GameCreated event = new GameCreated("game name", "lovely-dog-23");

        EventDto eventDto = EventDto.from(1, 12, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(
                        1,
                        12,
                        "GameCreated",
                        """
                        {"gameName":"game name","handle":"lovely-dog-23"}"""));

    }

    @ParameterizedTest
    @MethodSource("events")
    void gameEventRoundTripConversion(GameEvent sourceEvent) {
        EventDto eventDto = EventDto.from(3, 14, sourceEvent);

        GameEvent actual = eventDto.toDomain();

        assertThat(actual)
                .isEqualTo(sourceEvent);
    }

    public static Stream<Arguments> events() {
        return Stream.of(
                Arguments.of(new GameCreated("Name of Game", "happy-cat-87")),
                Arguments.of(new PlayerJoined(new MemberId(88L), "Chiddi")),
                Arguments.of(new GameStarted())
        );
    }
}