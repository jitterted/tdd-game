package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HexTileTest {

    @Test
    void fromWhatShouldItDoWhenDiscardCardReturnsHowWillYouKnowItDidIt() {
        HexTile whatShouldItDo = HexTile.WHAT_SHOULD_IT_DO;

        assertThat(whatShouldItDo.cardDiscarded())
                .isEqualByComparingTo(HexTile.HOW_WILL_YOU_KNOW_IT_DID_IT);
    }

    @Test
    void fromHowWillYouKnowItDidItWhenDiscardCardReturnsWriteCodeForTest() {
        HexTile howWillYouKnowItDidIt = HexTile.HOW_WILL_YOU_KNOW_IT_DID_IT;

        assertThat(howWillYouKnowItDidIt.cardDiscarded())
            .isEqualByComparingTo(HexTile.WRITE_CODE_FOR_TEST);
    }

    @Test
    void exceptionThrownWhenDiscardCardOnWriteCodeForTest() {
        HexTile writeCodeForTest = HexTile.WRITE_CODE_FOR_TEST;

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(writeCodeForTest::cardDiscarded)
                .withMessage("Probably want to return itself, but not sure yet");
    }
}