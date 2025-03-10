package dev.ted.tddgame.domain;

import java.util.List;

public record ActionCardDeckReplenished(List<Card> cardsInDrawPile) implements ActionCardDeckEvent, DeckReplenished {
    public ActionCardDeckReplenished(List<Card> cardsInDrawPile) {
        this.cardsInDrawPile = List.copyOf(cardsInDrawPile);
    }
}
