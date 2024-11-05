package dev.ted.tddgame.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.ActionCardDeckCreated;
import dev.ted.tddgame.domain.ActionCardDrawn;
import dev.ted.tddgame.domain.GameCreated;
import dev.ted.tddgame.domain.GameEvent;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerJoined;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Disabled("Until we have only non-generic Event records")
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

    @Test
    void objectMapperCorrectlyInstantiatesEventRecord() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        GameEvent noGenericsOriginal = new GameCreated("game name", "gameHandle");
        String noGenericsJson = objectMapper.writeValueAsString(noGenericsOriginal);
        GameEvent noGenericsDeserialized = objectMapper.readValue(noGenericsJson, GameCreated.class);
        assertThat(noGenericsDeserialized)
                .isEqualTo(noGenericsOriginal);

        ActionCardDrawn predictDrawnDirectInstantiation = new ActionCardDrawn(ActionCard.PREDICT);
        String json = objectMapper
                .writeValueAsString(predictDrawnDirectInstantiation);

        ActionCardDrawn actionCardDrawnFromJson = objectMapper
                .readValue(json, ActionCardDrawn.class);

        assertThat(predictDrawnDirectInstantiation.card().getClass())
                .as("Cards should be equal")
                .isEqualTo(actionCardDrawnFromJson.card().getClass());

        assertThat(actionCardDrawnFromJson)
                .as("Deserialized event is not the same as the original event object")
                .isEqualTo(predictDrawnDirectInstantiation);
    }

    @Test
    void allEventsAreTested() {
        Set<String> allGameEventClasses =
                Arrays.stream(GameEvent.class.getPermittedSubclasses())
                      .flatMap(cls -> cls.isInterface() ?
                              Arrays.stream(cls.getPermittedSubclasses()) :
                              Stream.of(cls))
                      .filter(cls -> !cls.isInterface() && !Modifier.isAbstract(cls.getModifiers()))
                      .map(Class::getSimpleName)
                      .collect(Collectors.toSet());

        Set<String> eventClassesCoveredByParameterizedTest = events()
                .map(arg -> arg.get()[0].getClass())
                .map(Class::getSimpleName)
                .collect(Collectors.toSet());

        assertThat(eventClassesCoveredByParameterizedTest)
                .as("Missing some GameEvents from the parameterized test")
                .containsAll(allGameEventClasses);
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
        MemberId memberId = new MemberId(88L);
        return Stream.of(
                Arguments.of(new GameCreated("Name of Game", "happy-cat-87"))
                , Arguments.of(new PlayerJoined(memberId, "Chiddi"))
                , Arguments.of(new GameStarted())
                , Arguments.of(new ActionCardDeckCreated(List.of(ActionCard.PREDICT)))
                , Arguments.of(new PlayerDrewActionCard(memberId, ActionCard.REFACTOR))
                , Arguments.of(new ActionCardDrawn(ActionCard.PREDICT))
        );
    }
}