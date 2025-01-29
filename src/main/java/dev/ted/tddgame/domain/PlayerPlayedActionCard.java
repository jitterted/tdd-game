package dev.ted.tddgame.domain;

public record PlayerPlayedActionCard(MemberId memberId, ActionCard actionCard)
        implements PlayerEvent {
}