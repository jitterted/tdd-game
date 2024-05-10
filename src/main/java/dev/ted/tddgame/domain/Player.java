package dev.ted.tddgame.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class Player {
    private final Long id;
    private final PersonId personId;

    public Player(Long id, PersonId personId) {
        this.id = id;
        this.personId = personId;
    }

    public Long id() {
        return id;
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

        if (this.id == null && player.id == null) {
            return false;
        }

        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .toString();
    }
}
