package dev.ted.tddgame.domain;

@FunctionalInterface
public interface EventEnqueuer {
    void enqueue(GameEvent gameEvent);
}
