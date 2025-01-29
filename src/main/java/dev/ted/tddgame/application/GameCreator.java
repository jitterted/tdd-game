package dev.ted.tddgame.application;

import com.github.kkuegler.HumanReadableIdGenerator;
import com.github.kkuegler.PermutationBasedHumanReadableIdGenerator;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;

// Application Use Case (aka Inbound Port) Command
public class GameCreator {

    private final HumanReadableIdGenerator idGen = new PermutationBasedHumanReadableIdGenerator();
    private final GameStore gameStore;

    public GameCreator(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    public static GameCreator createNull() {
        return new GameCreator(GameStore.createEmpty());
    }

    public static GameCreator create(GameStore gameStore) {
        return new GameCreator(gameStore);
    }

    public Game createNewGame(String nameOfGame) {
        String handle = idGen.generate();
        Game game = new Game.GameFactory().create(nameOfGame, handle);
        gameStore.save(game);
        return game;
    }

}
