package dev.ted.tddgame.domain;

import java.util.List;

public record ActionCardDeckReplenished(List<ActionCard> cardsInDrawPile)
        implements DeckEvent {
    public ActionCardDeckReplenished(List<ActionCard> cardsInDrawPile) {
        this.cardsInDrawPile = List.copyOf(cardsInDrawPile);
    }
}
