package dev.ted.tddgame.domain;

public record DeckCardDrawn<CARD>(CARD card) implements DeckEvent<CARD> {
}
