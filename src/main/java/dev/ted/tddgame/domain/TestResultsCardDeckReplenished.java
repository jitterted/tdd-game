package dev.ted.tddgame.domain;

import java.util.List;

public record TestResultsCardDeckReplenished(List<Card> cardsInDrawPile)
        implements TestResultsCardDeckEvent, DeckReplenished {
    public TestResultsCardDeckReplenished(List<Card> cardsInDrawPile) {
        this.cardsInDrawPile = List.copyOf(cardsInDrawPile);
    }
}
