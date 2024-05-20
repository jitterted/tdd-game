package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.PersonId;
import dev.ted.tddgame.domain.Player;

// Application Use Case (aka Inbound Port) Command
public class PlayerJoinsGame {

    private final GameStore gameStore;

    public PlayerJoinsGame(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    public static PlayerJoinsGame createNull() {
        return new PlayerJoinsGame(new GameStore());
    }

    // can return value, Command-Query Separation doesn't apply here
    // (as we're not in the Domain, managing Domain state)
    @Deprecated
    public Player join(Person person, Game game) {
        game.join(person.id());
        return game.playerFor(person.id());
    }

    public void join(PersonId personId, String gameHandle) {
//        gameStore.findByHandle(gameHandle)
    }
}
