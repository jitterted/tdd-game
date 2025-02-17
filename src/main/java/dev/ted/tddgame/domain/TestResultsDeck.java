package dev.ted.tddgame.domain;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestResultsDeck extends Deck<TestResultsCard> {
    public TestResultsDeck(List<TestResultsCard> testResultsCards, Shuffler<TestResultsCard> shuffler, EventEnqueuer eventEnqueuer) {
        super(testResultsCards, shuffler, eventEnqueuer);
    }

    TestResultsDeck(List<TestResultsCard> testResultsCards, Shuffler<TestResultsCard> shuffler, List<DeckEvent> deckEventsReceiver) {
        super(testResultsCards, shuffler, deckEventsReceiver);
    }

    static TestResultsDeck createForTest(List<@NotNull TestResultsCard> testResultsCards) {
        return new TestResultsDeck(
                testResultsCards,
                new IdentityShuffler<>(),
                new ArrayList<>()
        );
    }

    public static TestResultsDeck createForTest(TestResultsCard... testResultsCards) {
        return createForTest(Arrays.asList(testResultsCards));
    }
}
