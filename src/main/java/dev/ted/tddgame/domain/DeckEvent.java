package dev.ted.tddgame.domain;

public sealed interface DeckEvent extends GameEvent
        permits DeckReplenished, CardDiscarded, CardDrawn {
}
