package dev.ted.tddgame.domain;

import java.util.List;

public sealed interface DeckReplenished
        permits ActionCardDeckReplenished {
    List<Card> cardsInDrawPile();
}
