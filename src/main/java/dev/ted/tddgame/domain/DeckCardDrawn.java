package dev.ted.tddgame.domain;

public record DeckCardDrawn<CARD>(CARD card) implements DeckEvent<CARD> {
    public DeckCardDrawn {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
