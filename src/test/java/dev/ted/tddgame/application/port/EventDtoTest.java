package dev.ted.tddgame.application.port;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.ActionCardDeckCreated;
import dev.ted.tddgame.domain.ActionCardDeckReplenished;
import dev.ted.tddgame.domain.ActionCardDiscarded;
import dev.ted.tddgame.domain.ActionCardDrawn;
import dev.ted.tddgame.domain.Card;
import dev.ted.tddgame.domain.GameCreated;
import dev.ted.tddgame.domain.GameEvent;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.PlayerDiscardedActionCard;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerDrewTechNeglectCard;
import dev.ted.tddgame.domain.PlayerDrewTestResultsCard;
import dev.ted.tddgame.domain.PlayerJoined;
import dev.ted.tddgame.domain.PlayerPlayedActionCard;
import dev.ted.tddgame.domain.TestResultsCard;
import dev.ted.tddgame.domain.TestResultsCardDeckCreated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class EventDtoTest {

    @Test
    void ensureTwoWayMappingOfCardDrawnForActionAndTestResultsCards() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Card.class, CardMixIn.class);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        ActionCardDrawn actionCardDrawn = new ActionCardDrawn(ActionCard.WRITE_CODE);

        String json = objectMapper.writeValueAsString(actionCardDrawn);

        assertThat(objectMapper.readValue(json, ActionCardDrawn.class))
                .isEqualTo(actionCardDrawn);

        ActionCardDrawn testResultsCardDrawn = new ActionCardDrawn(TestResultsCard.NEED_ONE_LESS_CODE);

        json = objectMapper.writeValueAsString(testResultsCardDrawn);

        assertThat(objectMapper.readValue(json, ActionCardDrawn.class))
                .isEqualTo(testResultsCardDrawn);
    }

    @Test
    void showsPlayerDrewTechNeglectCardJson() {
        MemberId memberId = new MemberId(42L);
        PlayerDrewTechNeglectCard event = new PlayerDrewTechNeglectCard(memberId, ActionCard.CANT_ASSERT);

        EventDto eventDto = EventDto.from(1, 12, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(
                        1,
                        12,
                        "PlayerDrewTechNeglectCard",
                        """
                        {"memberId":{"id":42},"techNeglectActionCard":["ActionCard","CANT_ASSERT"]}"""));
    }

    @Test
    void showsPlayerDrewActionCardJson() {
        MemberId memberId = new MemberId(42L);
        PlayerDrewActionCard event = new PlayerDrewActionCard(memberId, ActionCard.REFACTOR);

        EventDto eventDto = EventDto.from(1, 12, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(
                        1,
                        12,
                        "PlayerDrewActionCard",
                        """
                        {"memberId":{"id":42},"actionCard":["ActionCard","REFACTOR"]}"""));
    }

    @Test
    void showsPlayerDrewTestResultsCardJson() {
        MemberId memberId = new MemberId(42L);
        PlayerDrewTestResultsCard event = new PlayerDrewTestResultsCard(memberId, TestResultsCard.NEED_ONE_LESS_CODE);

        EventDto eventDto = EventDto.from(1, 12, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(
                        1,
                        12,
                        "PlayerDrewTestResultsCard",
                        """
                        {"memberId":{"id":42},"testResultsCard":["TestResultsCard","NEED_ONE_LESS_CODE"]}"""));
    }

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
    void allEventsAreTested() {
        Set<String> allGameEventClasses = findAllConcreteGameEventClasses();

        Set<String> eventClassesCoveredByParameterizedTest = events()
                .map(arg -> arg.get()[0].getClass())
                .map(Class::getSimpleName)
                .collect(Collectors.toSet());

        assertThat(eventClassesCoveredByParameterizedTest)
                .as("Missing some GameEvents from the parameterized test")
                .containsAll(allGameEventClasses);
    }

    private Set<String> findAllConcreteGameEventClasses() {
        return findAllConcreteImplementations(GameEvent.class)
                .stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> findAllConcreteImplementations(Class<?> sealedInterface) {
        Set<Class<?>> result = new HashSet<>();

        if (!sealedInterface.isInterface() && !Modifier.isAbstract(sealedInterface.getModifiers())) {
            // This is a concrete class, add it
            result.add(sealedInterface);
            return result;
        }

        // Get direct permitted subclasses/interfaces
        Class<?>[] permittedSubclasses = sealedInterface.getPermittedSubclasses();
        if (permittedSubclasses == null) {
            return result;
        }

        // Recursively process each permitted subclass/interface
        for (Class<?> subclass : permittedSubclasses) {
            if (!subclass.isInterface() && !Modifier.isAbstract(subclass.getModifiers())) {
                // This is a concrete class, add it
                result.add(subclass);
            } else {
                // This is an interface or abstract class, recurse into it
                result.addAll(findAllConcreteImplementations(subclass));
            }
        }

        return result;
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
                , Arguments.of(new TestResultsCardDeckCreated(Collections.emptyList()))
                , Arguments.of(new PlayerDrewActionCard(memberId, ActionCard.REFACTOR))
                , Arguments.of(new ActionCardDrawn(ActionCard.PREDICT))
                , Arguments.of(new ActionCardDiscarded(ActionCard.LESS_CODE))
                , Arguments.of(new ActionCardDeckReplenished(
                        List.of(ActionCard.WRITE_CODE,
                                ActionCard.LESS_CODE,
                                ActionCard.CANT_ASSERT)
                ))
                , Arguments.of(new PlayerDiscardedActionCard(memberId, ActionCard.PREDICT))
                , Arguments.of(new PlayerPlayedActionCard(memberId, ActionCard.PREDICT))
                , Arguments.of(new PlayerDrewTechNeglectCard(memberId, ActionCard.CANT_ASSERT))
                , Arguments.of(new PlayerDrewTestResultsCard(memberId, TestResultsCard.NEED_ONE_LESS_CODE))
        );
    }
}
