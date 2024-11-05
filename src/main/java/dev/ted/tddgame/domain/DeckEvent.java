package dev.ted.tddgame.domain;

public sealed interface DeckEvent<CARD> extends GameEvent
        permits ActionCardDrawn, DeckReplenished {
}
