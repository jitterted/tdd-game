package dev.ted.tddgame.domain;

public sealed interface GameEvent
        permits ActionCardDeckCreated, ActionCardDeckReplenished, DeckEvent, GameCreated, GameStarted, PlayerEvent, PlayerJoined, TestResultsCardDeckCreated {
}
