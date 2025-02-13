package dev.ted.tddgame.domain;

public record CardDiscarded(Card card) implements DeckEvent {
    public CardDiscarded {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
