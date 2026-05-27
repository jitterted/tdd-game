package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TestResultsCardDeckTest {

    private static final MemberId IRRELEVANT_MEMBER_ID = new MemberId(42L);

    @Nested
    class CommandGeneratesEvent {

        @Test
        void drawFromReplenishedDrawPile_Emits_PlayerDrewTestResultsCard() {
            TestResultsCardDeck deck = TestResultsCardDeck.createForTest();
            deck.apply(new TestResultsCardDeckReplenished(
                    List.of(TestResultsCard.NEED_ONE_LESS_CODE,
                            TestResultsCard.NEED_TWO_LESS_CODE,
                            TestResultsCard.AS_PREDICTED)));

            List<GameEvent> gameEvents = deck.drawFor(IRRELEVANT_MEMBER_ID);

            assertThat(gameEvents)
                    .containsExactly(
                            new PlayerDrewTestResultsCard(
                                    IRRELEVANT_MEMBER_ID,
                                    TestResultsCard.NEED_ONE_LESS_CODE));
        }
    }

    @Nested
    class EventsProjectState {

        @Test
        void playerDrewTestResultsCard_CardRemovedFromDrawPile() {
            TestResultsCardDeck deck = TestResultsCardDeck.createForTest();
            deck.apply(new TestResultsCardDeckReplenished(
                    List.of(TestResultsCard.NEED_ONE_LESS_CODE,
                            TestResultsCard.NEED_TWO_LESS_CODE,
                            TestResultsCard.AS_PREDICTED)));
            deck.apply(new PlayerDrewTestResultsCard(IRRELEVANT_MEMBER_ID,
                    TestResultsCard.NEED_ONE_LESS_CODE));

            assertThat(deck.view().drawPile())
                    .containsExactly(TestResultsCard.NEED_TWO_LESS_CODE,
                                     TestResultsCard.AS_PREDICTED);
        }
    }
}