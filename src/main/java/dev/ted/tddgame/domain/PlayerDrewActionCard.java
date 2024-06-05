package dev.ted.tddgame.domain;

public record PlayerDrewActionCard(PlayerId playerId, ActionCard actionCard) implements GameEvent {
}
