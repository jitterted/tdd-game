package dev.ted.tddgame.domain;

public sealed interface GameEvent
        permits ActionCardDeckCreated, GameCreated, GameStarted, PlayerDrewActionCard, PlayerJoined {
}
