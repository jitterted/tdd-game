package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameStore {

    private final Map<String, Game> gameMap = new HashMap<>();

    public List<Game> findAll() {
        return gameMap.values().stream().toList();
    }

    public void save(Game game) {
        gameMap.put(game.handle(), game);
    }

    public Optional<Game> findByHandle(String gameHandle) {
        return Optional.ofNullable(gameMap.get(gameHandle));
    }
}
