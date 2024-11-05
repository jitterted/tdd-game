package dev.ted.tddgame.domain;

import java.util.List;

public record ActionCardDeckCreated(List<ActionCard> actionCards)
        implements GameEvent {
}
