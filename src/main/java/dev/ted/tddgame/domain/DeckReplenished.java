package dev.ted.tddgame.domain;

import java.util.List;

public record DeckReplenished(List<Card> cardsInDrawPile) implements DeckEvent {
    public DeckReplenished(List<Card> cardsInDrawPile) {
        this.cardsInDrawPile = List.copyOf(cardsInDrawPile);
    }
}
