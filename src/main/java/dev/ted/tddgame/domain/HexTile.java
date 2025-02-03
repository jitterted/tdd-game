package dev.ted.tddgame.domain;

public enum HexTile {
    WHAT_SHOULD_IT_DO("What Should It Do?") {
        @Override
        public HexTile cardDiscarded() {
            return HOW_WILL_YOU_KNOW_IT_DID_IT;
        }
    }
    , HOW_WILL_YOU_KNOW_IT_DID_IT("How Will You Know It Did It?") {
        @Override
        public HexTile cardDiscarded() {
            return WRITE_CODE_FOR_TEST;
        }
    }, WRITE_CODE_FOR_TEST("Write Code for Test") {
        @Override
        public HexTile cardDiscarded() {
            throw new UnsupportedOperationException("Probably want to return itself, but not sure yet");
        }

        @Override
        public HexTile cardPlayed(ActionCard actionCard) {
            return PREDICT_TEST_WILL_FAIL_TO_COMPILE;
        }
    }, PREDICT_TEST_WILL_FAIL_TO_COMPILE("Predict Test Will Fail to Compile") {
        @Override
        public HexTile cardDiscarded() {
            throw new UnsupportedOperationException();
        }

        @Override
        public HexTile cardPlayed(ActionCard actionCard) {
            return this;
        }
    };

    private final String title;

    HexTile(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }

    public abstract HexTile cardDiscarded();

    public HexTile cardPlayed(ActionCard actionCard) {
        throw new UnsupportedOperationException();
    }
}
