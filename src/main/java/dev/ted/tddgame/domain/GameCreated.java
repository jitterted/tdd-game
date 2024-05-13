package dev.ted.tddgame.domain;

public record GameCreated(String gameName, String handle) implements GameEvent {
}
