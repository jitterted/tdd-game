package dev.ted.tddgame.domain;

public record PlayerJoined(PersonId personId) implements GameEvent {
}
