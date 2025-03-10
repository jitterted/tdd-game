package dev.ted.tddgame.domain;

import java.util.List;

public sealed interface DeckReplenished
        permits ActionCardDeckReplenished, TestResultsCardDeckReplenished {
    List<Card> cardsInDrawPile();
}
