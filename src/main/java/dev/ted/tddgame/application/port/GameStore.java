package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameStore {

    private final Map<String, List<EventDto>> handleToEventDtoMap = new HashMap<>();

    public static GameStore createEmpty() {
        return new GameStore();
    }

    private GameStore() {
    }

    public List<Game> findAll() {
        return handleToEventDtoMap
                .keySet()
                .stream()
                .map(this::reconstitute)
                .toList();
    }

    public void save(Game game) {
        List<EventDto> existingEventDtos = handleToEventDtoMap
                .computeIfAbsent(game.handle(),
                                 (_) -> new ArrayList<>());
//        AtomicInteger index = new AtomicInteger(game.lastEventId() + 1);
        List<EventDto> freshEventDtos = game.freshEvents()
                                            .map(event -> EventDto.from(
                                                    42,
                                                    0,
                                                    event))
                                            .toList();
        existingEventDtos.addAll(freshEventDtos);

        handleToEventDtoMap.put(game.handle(), existingEventDtos);
    }

    public Optional<Game> findByHandle(String gameHandle) {
        if (handleToEventDtoMap.containsKey(gameHandle)) {
            Game game = reconstitute(gameHandle);
            return Optional.of(game);
        } else {
            return Optional.empty();
        }
    }

    private Game reconstitute(String gameHandle) {
        List<GameEvent> gameEvents = handleToEventDtoMap
                .get(gameHandle)
                .stream()
                .map(EventDto::toDomain)
                .toList();
        return Game.reconstitute(gameEvents);
    }
}
