package dev.ted.tddgame.domain;

public record ActionCardDrawn(ActionCard card) implements DeckEvent<ActionCard> {
    public ActionCardDrawn {
        if (card == null) {
            throw new IllegalArgumentException("CARD must not be null");
        }
    }
}
