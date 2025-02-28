package dev.ted.tddgame.domain;

public record PlayerDrewTestResultsCard(MemberId memberId, TestResultsCard testResultsCard)
        implements PlayerEvent {}