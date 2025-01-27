package dev.ted.tddgame.domain;

public enum HexTile {
    WHAT_SHOULD_IT_DO("What Should It Do?")
    , HOW_WILL_YOU_KNOW_IT_DID_IT("How Will You Know It Did It?")
    ;

    private final String title;

    HexTile(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    public HexTile discardCard() {
        return HOW_WILL_YOU_KNOW_IT_DID_IT;
    }
}
