package dev.ted.tddgame.domain;

public record PlayerJoined(MemberId memberId) implements GameEvent {
}
