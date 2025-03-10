package dev.ted.tddgame.domain;

public sealed interface CardDrawn permits ActionCardDrawn, TestResultsCardDrawn {
    Card card();
}
