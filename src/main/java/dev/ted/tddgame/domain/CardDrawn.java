package dev.ted.tddgame.domain;

public sealed interface CardDrawn permits ActionCardDrawn {
    Card card();
}
