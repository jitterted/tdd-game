package dev.ted.tddgame.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class Player {
    private final PersonId personId;
    private final PlayerId playerId;

    public Player(PersonId personId, PlayerId playerId) {
        this.playerId = playerId;
        this.personId = personId;
    }

    public PlayerId id() {
        return playerId;
    }

    public PersonId personId() {
        return personId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player player)) {
            return false;
        }

        if (this.playerId.id() == null && player.playerId.id() == null) {
            return false;
        }

        return Objects.equals(playerId.id(), player.playerId.id());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId.id());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("id=" + playerId.id())
                .toString();
    }
}
