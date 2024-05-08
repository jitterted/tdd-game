package dev.ted.tddgame.application;

import dev.ted.tddgame.domain.Game;

public class GameCreator {

    public Game createNewGame(String nameOfGame) {
        return new Game(nameOfGame);
    }

}
