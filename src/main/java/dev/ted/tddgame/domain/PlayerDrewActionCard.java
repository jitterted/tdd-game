package dev.ted.tddgame.domain;

public record PlayerDrewActionCard(MemberId memberId, ActionCard actionCard)
        implements PlayerEvent {}
