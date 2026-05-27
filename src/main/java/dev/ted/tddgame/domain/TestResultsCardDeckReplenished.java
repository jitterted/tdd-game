package dev.ted.tddgame.domain;

import java.util.List;

public record TestResultsCardDeckReplenished(List<Card> cardsInDrawPile)
        implements DeckReplenished, DeckEvent {
    public TestResultsCardDeckReplenished(List<Card> cardsInDrawPile) {
        this.cardsInDrawPile = List.copyOf(cardsInDrawPile);
    }
}
