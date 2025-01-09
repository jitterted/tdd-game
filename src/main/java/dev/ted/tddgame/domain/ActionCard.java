package dev.ted.tddgame.domain;

public enum ActionCard {
    LESS_CODE("Less Code"),
    WRITE_CODE("Write Code"),
    REFACTOR("Refactor"),
    PREDICT("Predict"),

    // "tech debt/neglect" (aka "red") cards
    CODE_BLOAT("Code Bloat"),
    CANT_ASSERT("Can't Assert"),
    ;

    private final String title;

    ActionCard(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
