package dev.ted.tddgame.domain;

public enum ActionCard {
    LESS_CODE("less code"),
    WRITE_CODE("write code"),
    REFACTOR("refactor"),
    PREDICT("predict"),

    // "tech debt/neglect" (aka "red") cards
    CODE_BLOAT("code bloat"),
    CANT_ASSERT("can't assert"),
    ;

    private final String title;

    ActionCard(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
