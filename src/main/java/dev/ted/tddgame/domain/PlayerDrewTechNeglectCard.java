package dev.ted.tddgame.domain;

public record PlayerDrewTechNeglectCard(MemberId memberId,
                                        ActionCard techNeglectActionCard)
        implements PlayerEvent {}