package dev.ted.tddgame.domain;

public record ActionCardDrawn(Card card) implements CardDrawn, ActionCardDeckEvent {
    public ActionCardDrawn {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
