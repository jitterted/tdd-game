package dev.ted.tddgame.domain;

import java.util.List;

public record DeckReplenished<CARD>(List<CARD> cardsInDrawPile) implements DeckEvent {
}
