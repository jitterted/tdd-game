package dev.ted.tddgame.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ted.tddgame.domain.ActionCardDeckCreated;
import dev.ted.tddgame.domain.ActionCardDeckReplenished;
import dev.ted.tddgame.domain.ActionCardDiscarded;
import dev.ted.tddgame.domain.ActionCardDrawn;
import dev.ted.tddgame.domain.Card;
import dev.ted.tddgame.domain.GameCreated;
import dev.ted.tddgame.domain.GameEvent;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.PlayerDiscardedActionCard;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerDrewTechNeglectCard;
import dev.ted.tddgame.domain.PlayerDrewTestResultsCard;
import dev.ted.tddgame.domain.PlayerJoined;
import dev.ted.tddgame.domain.PlayerPlayedActionCard;
import dev.ted.tddgame.domain.TestResultsCardDeckCreated;
import dev.ted.tddgame.domain.TestResultsCardDeckReplenished;
import dev.ted.tddgame.domain.TestResultsCardDrawn;

import java.util.HashMap;
import java.util.Map;

/*
    Table schema for storing in a database:

    PK EntityId + EventId
    JSON String eventContent

    EntityID | EventId  | EventType         |  JSON Content
    -----------------------------------------------------------------
    0        | 0        | PlayerRegistered  | { name: "Judy" }
    0        | 1        | MoneyDeposited    | { amount: 10 }
*/

public class EventDto {
    private final int entityId; // ID for the Aggregate Root
    private final int eventId;
    private final String eventType;
    private final String json;

    // -- the following mapper and maps should be externalized to some configuration
    //    so that when adding (and especially renaming) classes, the mapping works
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Class<? extends GameEvent>> eventNameToClassMap =
            Map.ofEntries(
                    Map.entry("GameCreated", GameCreated.class),
                    Map.entry("PlayerJoined", PlayerJoined.class),
                    Map.entry("GameStarted", GameStarted.class),
                    Map.entry("ActionCardDeckCreated", ActionCardDeckCreated.class),
                    Map.entry("TestResultsCardDeckCreated", TestResultsCardDeckCreated.class),
                    Map.entry("PlayerDrewActionCard", PlayerDrewActionCard.class),
                    Map.entry("PlayerDiscardedActionCard", PlayerDiscardedActionCard.class),
                    Map.entry("PlayerPlayedActionCard", PlayerPlayedActionCard.class),
                    Map.entry("PlayerDrewTechNeglectCard", PlayerDrewTechNeglectCard.class),
                    Map.entry("PlayerDrewTestResultsCard", PlayerDrewTestResultsCard.class),
                    Map.entry("ActionCardDeckReplenished", ActionCardDeckReplenished.class),
                    Map.entry("ActionCardDrawn", ActionCardDrawn.class),
                    Map.entry("ActionCardDiscarded", ActionCardDiscarded.class),
                    Map.entry("TestResultsCardDeckReplenished", TestResultsCardDeckReplenished.class),
                    Map.entry("TestResultsCardDrawn", TestResultsCardDrawn.class)
            );
    private static final Map<Class<? extends GameEvent>, String> classToEventName =
            swapKeysValues(eventNameToClassMap);

    private static <K, V> Map<V, K> swapKeysValues(Map<K, V> map) {
        Map<V, K> swapped = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            swapped.put(entry.getValue(), entry.getKey());
        }
        return swapped;
    }

    public EventDto(int entityId, int eventId, String eventClassName, String json) {
        if (eventClassName == null) {
            throw new IllegalArgumentException("Event class name cannot be null, JSON is: " + json);
        }
        this.entityId = entityId;
        this.eventId = eventId;
        this.eventType = eventClassName;
        this.json = json;
        objectMapper.addMixIn(Card.class, CardMixIn.class);
    }

    public static EventDto from(int entityId, int eventId, GameEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Card.class, CardMixIn.class);
        try {
            String json = objectMapper.writeValueAsString(event);
            String className = classToEventName.get(event.getClass());
            if (className == null) {
                throw new IllegalArgumentException("Unknown event class: " + event.getClass().getSimpleName());
            }
            return new EventDto(entityId, eventId, className, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int getEntityId() {
        return entityId;
    }

    public int getEventId() {
        return eventId;
    }

    public GameEvent toDomain() {
        try {
            return objectMapper.readValue(json, eventNameToClassMap.get(eventType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Problem convert JSON: " + json + " to " + eventType, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDto eventDto = (EventDto) o;

        if (entityId != eventDto.entityId) return false;
        if (eventId != eventDto.eventId) return false;
        if (!eventType.equals(eventDto.eventType)) return false;
        return json.equals(eventDto.json);
    }

    @Override
    public int hashCode() {
        int result = entityId;
        result = 31 * result + eventId;
        result = 31 * result + eventType.hashCode();
        result = 31 * result + json.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EventDto: {entityId=%d, eventId=%d, eventType='%s', json='%s'}"
                .formatted(entityId, eventId, eventType, json);
    }}
