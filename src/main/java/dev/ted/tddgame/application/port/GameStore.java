package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;

import java.util.ArrayList;
import java.util.List;

public class GameStore {

    private final ArrayList<Game> games = new ArrayList<>();

    public List<Game> findAll() {
        return games;
    }

    public void save(Game game) {
        games.add(game);
    }
}
