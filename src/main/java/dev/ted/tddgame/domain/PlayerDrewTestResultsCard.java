package dev.ted.tddgame.domain;

import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public record PlayerDrewTestResultsCard(MemberId memberId,
                                        TestResultsCard testResultsCard)
        implements PlayerEvent {
    public PlayerDrewTestResultsCard {
        Objects.requireNonNull(memberId);
        Objects.requireNonNull(testResultsCard);
    }
}