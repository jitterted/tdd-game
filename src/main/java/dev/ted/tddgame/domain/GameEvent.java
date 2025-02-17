package dev.ted.tddgame.domain;

public sealed interface GameEvent
        permits ActionCardDeckCreated, DeckEvent,
        GameCreated, GameStarted, PlayerEvent, PlayerJoined,
        TestResultsCardDeckCreated {
}
