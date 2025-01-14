package dev.ted.tddgame.domain;

public record ActionCardDiscarded(ActionCard card) implements DeckEvent {
    public ActionCardDiscarded {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
