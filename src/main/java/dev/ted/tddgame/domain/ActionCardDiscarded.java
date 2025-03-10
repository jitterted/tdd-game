package dev.ted.tddgame.domain;

public record ActionCardDiscarded(Card card) implements ActionCardDeckEvent, CardDiscarded {
    public ActionCardDiscarded {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
