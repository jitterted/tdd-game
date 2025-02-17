package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TestResultsDeckTestCard {

    @Test
    void deckHoldsTestResultsCardsThatCanBeDrawn() {
        TestResultsCardDeck testResultsCardDeck = TestResultsCardDeck.createForTest(
                TestResultsCard.NEED_ONE_LESS_CODE,
                TestResultsCard.NEED_TWO_LESS_CODE,
                TestResultsCard.AS_PREDICTED
        );

        assertThat(testResultsCardDeck.draw())
                .isEqualByComparingTo(TestResultsCard.NEED_ONE_LESS_CODE);
        assertThat(testResultsCardDeck.draw())
                .isEqualByComparingTo(TestResultsCard.NEED_TWO_LESS_CODE);
        assertThat(testResultsCardDeck.draw())
                .isEqualByComparingTo(TestResultsCard.AS_PREDICTED);

        testResultsCardDeck.acceptDiscard(TestResultsCard.NEED_ONE_LESS_CODE);
        assertThat(testResultsCardDeck.draw())
                .isEqualByComparingTo(TestResultsCard.NEED_ONE_LESS_CODE);
    }

}