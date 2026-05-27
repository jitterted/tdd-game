package dev.ted.tddgame.domain;

import java.util.List;

public record ActionCardDeckReplenished(List<? extends Card> cardsInDrawPile) implements ActionCardDeckEvent, DeckReplenished {
    public ActionCardDeckReplenished(List<? extends Card> cardsInDrawPile) {
        this.cardsInDrawPile = List.copyOf(cardsInDrawPile);
    }
}
