package dev.ted.tddgame.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ted.tddgame.domain.GameCreated;
import dev.ted.tddgame.domain.GameEvent;
import dev.ted.tddgame.domain.GameStarted;
import dev.ted.tddgame.domain.PlayerJoined;

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
    private static final Map<String, Class<? extends GameEvent>> eventNameToClassMap = Map.of(
            "GameCreated", GameCreated.class,
            "PlayerJoined", PlayerJoined.class,
            "GameStarted", GameStarted.class
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


    public EventDto(int entityId, int eventId, String eventType, String json) {
        this.entityId = entityId;
        this.eventId = eventId;
        this.eventType = eventType;
        this.json = json;
    }

    public static EventDto from(int entityId, int eventId, GameEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(event);
            String className = classToEventName.get(event.getClass());
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
            throw new RuntimeException(e);
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
