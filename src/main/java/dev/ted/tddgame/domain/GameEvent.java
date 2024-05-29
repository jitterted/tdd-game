package dev.ted.tddgame.domain;

public sealed interface GameEvent
        permits GameCreated, GameStarted, PlayerJoined {
}
