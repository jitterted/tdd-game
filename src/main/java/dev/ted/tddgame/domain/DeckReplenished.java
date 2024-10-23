package dev.ted.tddgame.domain;

import java.util.List;

public record DeckReplenished<CARD>(List<CARD> cardsInDrawPile)
        implements DeckEvent<CARD> {
    public DeckReplenished(List<CARD> cardsInDrawPile) {
        this.cardsInDrawPile = List.copyOf(cardsInDrawPile);
    }
}
