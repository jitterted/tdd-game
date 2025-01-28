package dev.ted.tddgame.domain;

import java.util.List;

public class GameFactory {
    /**
     * Create a Game by playing back all of the events and applying them.
     * MUST only be called by the repository (store), or tests
     *
     * @param events GameEvents to play back
     */
    public Game reconstitute(List<GameEvent> events) {
        return new Game(events);
    }

}
