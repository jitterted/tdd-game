package dev.ted.tddgame.domain;

public enum HexTile {
    WHAT_SHOULD_IT_DO("What Should It Do?");

    private final String title;

    HexTile(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
