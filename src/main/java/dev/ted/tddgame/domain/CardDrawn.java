package dev.ted.tddgame.domain;

public record CardDrawn(Card card) implements DeckEvent {
    public CardDrawn {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
