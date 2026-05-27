package dev.ted.tddgame.domain;

import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NullMarked
public class TestResultsCardDeck extends Deck<TestResultsCard> {
    private TestResultsCardDeck(List<TestResultsCard> testResultsCards, Shuffler<TestResultsCard> shuffler, EventEnqueuer eventEnqueuer) {
        super(testResultsCards, shuffler, eventEnqueuer);
    }

    private TestResultsCardDeck(List<TestResultsCard> testResultsCards, Shuffler<TestResultsCard> shuffler, List<DeckEvent> deckEventsReceiver) {
        super(testResultsCards, shuffler, deckEventsReceiver);
    }

    public static TestResultsCardDeck create(List<TestResultsCard> testResultsCards, EventEnqueuer eventEnqueuer, Shuffler<TestResultsCard> shuffler) {
        return new TestResultsCardDeck(testResultsCards, shuffler, eventEnqueuer);
    }

    @Override
    protected DeckEvent createCardDiscardedEvent(TestResultsCard discardedCard) {
        return new ActionCardDiscarded(discardedCard);
    }

    @Override
    protected GameEvent createPlayerDrewCard(MemberId memberId, TestResultsCard drawnCard) {
        return new PlayerDrewTestResultsCard(memberId, drawnCard);
    }

    @Override
    protected DeckEvent createDeckReplenishedEvent(List<Card> shuffledDiscardedCards) {
        return new TestResultsCardDeckReplenished(shuffledDiscardedCards);
    }

    // -- FOR TESTS ONLY BELOW --

    static TestResultsCardDeck createForTest(List<TestResultsCard> testResultsCards) {
        return new TestResultsCardDeck(
                testResultsCards,
                new IdentityShuffler<>(),
                new ArrayList<>()
        );
    }

    public static TestResultsCardDeck createForTest(TestResultsCard... testResultsCards) {
        return createForTest(Arrays.asList(testResultsCards));
    }
}
