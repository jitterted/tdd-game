package dev.ted.tddgame.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

public sealed abstract class ActionCard permits RegularCard, TechNeglectCard {
    public static final RegularCard LESS_CODE = new RegularCard("Less Code", "LESS_CODE");
    public static final RegularCard WRITE_CODE = new RegularCard("Write Code", "WRITE_CODE");
    public static final RegularCard REFACTOR = new RegularCard("Refactor", "REFACTOR");
    public static final RegularCard PREDICT = new RegularCard("Predict", "PREDICT");

    // "tech debt/neglect" (aka "red") cards
    public static final TechNeglectCard CODE_BLOAT = new TechNeglectCard("Code Bloat", "CODE_BLOAT");
    public static final TechNeglectCard CANT_ASSERT = new TechNeglectCard("Can't Assert", "CANT_ASSERT");

    private static final Map<String, ActionCard> VALUES = Map.of(
        LESS_CODE.name(), LESS_CODE,
        WRITE_CODE.name(), WRITE_CODE,
        REFACTOR.name(), REFACTOR,
        PREDICT.name(), PREDICT,
        CODE_BLOAT.name(), CODE_BLOAT,
        CANT_ASSERT.name(), CANT_ASSERT
    );

    private final String title;
    private final String name;

    protected ActionCard(String title, String name) {
        this.title = title;
        this.name = name;
    }

    @JsonCreator
    public static ActionCard valueOf(String name) {
        ActionCard card = VALUES.get(name);
        if (card == null) {
            throw new IllegalArgumentException("No ActionCard with name: " + name);
        }
        return card;
    }

    @JsonValue
    public String name() {
        return name;
    }

    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return name();
    }

    abstract PlayerEvent drawnCardEventFor(MemberId memberId);
}
