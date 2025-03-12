package dev.ted.tddgame.domain;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum HexTile {
    WHAT_SHOULD_IT_DO("What Should It Do?") {
        @Override
        public HexTile cardDiscarded() {
            return HOW_WILL_YOU_KNOW_IT_DID_IT;
        }

        @Override
        public HexTile cardPlayed(ActionCard actionCard) {
            String cardName = actionCard.title();
            String tileName = title();
            throw new CanNotPlayCardHereException("Can not play a " + cardName + " card on the " + tileName + " hex tile.");
        }
    }, HOW_WILL_YOU_KNOW_IT_DID_IT("How Will You Know It Did It?") {
        @Override
        public HexTile cardDiscarded() {
            return WRITE_CODE_FOR_TEST;
        }

        @Override
        public HexTile cardPlayed(ActionCard actionCard) {
            String cardName = actionCard.title();
            String tileName = title();
            throw new CanNotPlayCardHereException("Can not play a " + cardName + " card on the " + tileName + " hex tile.");
        }
    }, WRITE_CODE_FOR_TEST("Write Code for Test") {
        @Override
        public HexTile cardDiscarded() {
            // allowed to discard, but doesn't move pawn
            return this;
        }

        @Override
        public HexTile cardPlayed(ActionCard actionCard) {
            return PREDICT_TEST_WILL_FAIL_TO_COMPILE;
        }
    }, PREDICT_TEST_WILL_FAIL_TO_COMPILE("Predict Test Will Fail to Compile") {
        @Override
        public HexTile cardDiscarded() {
            return this;
        }

        @Override
        public HexTile cardPlayed(ActionCard actionCard) {
            return this;
        }

        @Override
        public HexTile processTestResultsCard(TestResultsCard drawnTestResultsCard) {
            return WRITE_CODE_SO_TEST_COMPILES;
        }
    }, WRITE_CODE_SO_TEST_COMPILES("Write Code So Test Compiles") {
        @Override
        public HexTile cardDiscarded() {
            throw new UnsupportedOperationException();
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

    public HexTile processTestResultsCard(TestResultsCard drawnTestResultsCard) {
        throw new UnsupportedOperationException();
    }
}
