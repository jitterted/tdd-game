package dev.ted.tddgame.domain;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    protected DeckEvent createCardDrawnEvent(TestResultsCard drawnCard) {
        return new ActionCardDrawn(drawnCard);
    }

    // -- FOR TESTS ONLY BELOW --

    static TestResultsCardDeck createForTest(List<@NotNull TestResultsCard> testResultsCards) {
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
