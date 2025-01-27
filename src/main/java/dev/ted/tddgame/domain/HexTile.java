package dev.ted.tddgame.domain;

public enum HexTile {
    WHAT_SHOULD_IT_DO("What Should It Do?") {
        @Override
        public HexTile discardCard() {
            return HOW_WILL_YOU_KNOW_IT_DID_IT;
        }
    }
    , HOW_WILL_YOU_KNOW_IT_DID_IT("How Will You Know It Did It?") {
        @Override
        public HexTile discardCard() {
            return WRITE_CODE_FOR_TEST;
        }
    }, WRITE_CODE_FOR_TEST("Write Code for Test") {
        @Override
        public HexTile discardCard() {
            throw new UnsupportedOperationException("Probably want to return itself, but not sure yet");
        }
    };

    private final String title;

    HexTile(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    public abstract HexTile discardCard();
}
