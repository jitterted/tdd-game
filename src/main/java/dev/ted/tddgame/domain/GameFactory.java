package dev.ted.tddgame.domain;

import java.util.List;

public class GameFactory {

    protected final Deck.Shuffler<ActionCard> actionCardShuffler;

    public GameFactory() {
        this(new Deck.RandomShuffler<>());
    }

    public GameFactory(Deck.Shuffler<ActionCard> actionCardShuffler) {
        this.actionCardShuffler = actionCardShuffler;
    }

    /**
     * Create a Game by playing back all of the events and applying them.
     * MUST only be called by the repository (store), or tests.
     * Configures the Shuffler as RandomShuffler for production use
     *
     * @param events GameEvents to play back
     */
    public Game reconstitute(List<GameEvent> events) {
        return new Game(events, actionCardShuffler);
    }

}
