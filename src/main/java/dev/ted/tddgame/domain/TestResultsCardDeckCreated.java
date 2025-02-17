package dev.ted.tddgame.domain;

import java.util.List;

public record TestResultsCardDeckCreated(List<TestResultsCard> testResultsCards)
        implements GameEvent {
}
