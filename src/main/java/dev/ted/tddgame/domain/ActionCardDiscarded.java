package dev.ted.tddgame.domain;

@Deprecated // use Player event instead
public record ActionCardDiscarded(Card card) implements ActionCardDeckEvent, CardDiscarded {
    public ActionCardDiscarded {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
