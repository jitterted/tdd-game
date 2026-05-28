package dev.ted.tddgame.domain;

// @formatter:off
public sealed interface GameEvent permits
        ActionCardDeckCreated,
        ActionCardDeckReplenished,
        GameCreated,
        GameStarted,
        PlayerDiscardedActionCard,
        PlayerDrewActionCard,
        PlayerDrewTechNeglectCard,
        PlayerDrewTestResultsCard,
        PlayerJoined,
        PlayerPlayedActionCard,
        TestResultsCardDeckCreated,
        TestResultsCardDeckReplenished
{}
// @formatter:on
