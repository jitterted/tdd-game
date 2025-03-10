package dev.ted.tddgame.domain;

public record TestResultsCardDrawn(Card card) implements CardDrawn, TestResultsCardDeckEvent {
    public TestResultsCardDrawn {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
