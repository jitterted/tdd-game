package dev.ted.tddgame.application;

import com.github.kkuegler.HumanReadableIdGenerator;
import com.github.kkuegler.PermutationBasedHumanReadableIdGenerator;
import dev.ted.tddgame.domain.Game;

public class GameCreator {

    private final HumanReadableIdGenerator idGen = new PermutationBasedHumanReadableIdGenerator();

    public static GameCreator create() {
        return new GameCreator();
    }

    public Game createNewGame(String nameOfGame) {
        String handle = idGen.generate();

        return Game.create(nameOfGame, handle);
    }

}
