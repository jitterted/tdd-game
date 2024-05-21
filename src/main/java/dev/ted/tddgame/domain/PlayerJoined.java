package dev.ted.tddgame.domain;

public record PlayerJoined(MemberId memberId, String playerName) implements GameEvent {
}
