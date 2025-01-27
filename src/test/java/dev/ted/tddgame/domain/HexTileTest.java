package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HexTileTest {

    @Test
    void fromWhatShouldItDoWhenDiscardCardReturnsHowWillYouKnowItDidIt() {
        HexTile whatShouldItDo = HexTile.WHAT_SHOULD_IT_DO;

        assertThat(whatShouldItDo.discardCard())
                .isEqualByComparingTo(HexTile.HOW_WILL_YOU_KNOW_IT_DID_IT);
    }
}