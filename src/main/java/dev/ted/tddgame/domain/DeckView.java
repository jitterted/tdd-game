package dev.ted.tddgame.domain;

import java.util.List;

public record DeckView<T>(List<T> drawPile, List<T> discardPile) {
}
