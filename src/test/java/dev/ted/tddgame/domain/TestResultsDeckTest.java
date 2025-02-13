package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TestResultsDeckTest {

    @Test
    void deckHoldsTestResultsCardsThatCanBeDrawn() {
        Deck<TestResultsCard> testResultsCardDeck = new Deck<>(
                List.of(
                        TestResultsCard.NEED_ONE_LESS_CODE,
                        TestResultsCard.NEED_TWO_LESS_CODE,
                        TestResultsCard.AS_PREDICTED
                ),
                new Deck.IdentityShuffler<>(),
                new ArrayList<>()
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