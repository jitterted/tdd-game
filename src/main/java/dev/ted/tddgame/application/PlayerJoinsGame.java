package dev.ted.tddgame.application;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.Player;

// Application Use Case (aka Inbound Port) Command
public class PlayerJoinsGame {

    // can return value, Command-Query Separation doesn't apply here
    // (as we're not in the Domain, managing Domain state)
    public Player join(Person person, Game game) {
        game.join(person.id());
        return game.playerFor(person.id());
    }

}
