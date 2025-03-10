package dev.ted.tddgame.domain;

public sealed interface ActionCardDeckEvent extends DeckEvent
        permits ActionCardDiscarded, ActionCardDrawn, ActionCardDeckReplenished {
}
