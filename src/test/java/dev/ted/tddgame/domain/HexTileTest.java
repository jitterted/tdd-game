package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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
    void playLessCodeCardOnPredictTestWillFailToCompileReturnsSelfHexTile() {
        HexTile predictTestWillFailToCompile = HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE;

        assertThat(predictTestWillFailToCompile.cardPlayed(ActionCard.LESS_CODE))
                .isEqualByComparingTo(predictTestWillFailToCompile);
    }

    @Test
    void exceptionThrownWhenPlayCardOnWhatShouldItDoTile() {
        HexTile whatShouldItDo = HexTile.WHAT_SHOULD_IT_DO;

        assertThatExceptionOfType(CanNotPlayCardHereException.class)
                .isThrownBy(() ->
                                    whatShouldItDo
                                            .cardPlayed(ActionCard.LESS_CODE))
                .withMessage("Can not play a Less Code card on the What Should It Do? hex tile.");
    }

    @Test
    void exceptionThrownWhenPlayCardOnHowWillYouKnowTile() {
        HexTile howWillYouKnowItDidIt = HexTile.HOW_WILL_YOU_KNOW_IT_DID_IT;

        assertThatExceptionOfType(CanNotPlayCardHereException.class)
                .isThrownBy(() ->
                                    howWillYouKnowItDidIt
                                            .cardPlayed(ActionCard.WRITE_CODE))
                .withMessage("Can not play a Write Code card on the How Will You Know It Did It? hex tile.");
    }

    @ParameterizedTest
    @EnumSource(names = {
            "WRITE_CODE_FOR_TEST",
            "PREDICT_TEST_WILL_FAIL_TO_COMPILE"
    })
    void remainOnSameTileWhenDiscardCardOnWriteCodeForTest(HexTile hexTile) {
        HexTile nextTile = hexTile.cardDiscarded();

        assertThat(nextTile)
                .isEqualByComparingTo(hexTile);
    }
}