package dev.ted.tddgame.domain;

public sealed interface GameEvent
        permits ActionCardDeckCreated,
                ActionCardDeckReplenished,
                GameCreated,
                GameStarted,
                PlayerEvent,
                PlayerJoined,
                TestResultsCardDeckCreated,
                TestResultsCardDeckReplenished {
}
