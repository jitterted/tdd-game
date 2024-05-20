package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameStore {

    private final Map<String, Game> gameMap = new HashMap<>();

    public List<Game> findAll() {
        return gameMap.values().stream().toList();
    }

    public void save(Game game) {
        gameMap.put(game.handle(), game);
    }

    public Game findByHandle(String gameHandle) {
        return gameMap.get(gameHandle);
    }
}
