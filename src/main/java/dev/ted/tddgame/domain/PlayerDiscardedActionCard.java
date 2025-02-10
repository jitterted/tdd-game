package dev.ted.tddgame.domain;

public record PlayerDiscardedActionCard(MemberId memberId, ActionCard actionCard)
        implements PlayerEvent {}
