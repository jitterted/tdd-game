package dev.ted.tddgame.application;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.Player;

public class PlayerJoinsGame {

    public Player join(Person person, Game game) {
        return game.join(person);
    }

}
