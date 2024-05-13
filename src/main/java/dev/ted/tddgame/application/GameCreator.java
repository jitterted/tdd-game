package dev.ted.tddgame.application;

import com.github.kkuegler.HumanReadableIdGenerator;
import com.github.kkuegler.PermutationBasedHumanReadableIdGenerator;
import dev.ted.tddgame.application.port.GameViewLoader;
import dev.ted.tddgame.domain.Game;

public class GameCreator {

    private final HumanReadableIdGenerator idGen = new PermutationBasedHumanReadableIdGenerator();

    private GameCreator(GameViewLoader gameViewLoader) {
    }

    public static GameCreator create() {
        return new GameCreator(new GameViewLoader());
    }

    public static GameCreator create(GameViewLoader gameViewLoader) {
        return new GameCreator(gameViewLoader);
    }

    public Game createNewGame(String nameOfGame) {
        String handle = idGen.generate();

        return new Game(nameOfGame, handle);
    }

}
