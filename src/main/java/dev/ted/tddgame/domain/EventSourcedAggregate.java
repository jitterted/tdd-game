package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class EventSourcedAggregate {

    private final List<GameEvent> freshEvents = new ArrayList<>();

    protected void enqueue(GameEvent event) {
        addEvent(event);
        apply(event);
    }

    /**
     * Adds an event to the Fresh (uncommitted) Events list without applying it
     */
    protected void addEvent(GameEvent event) {
        freshEvents.add(event);
    }

    protected void addEvents(List<GameEvent> events) {
        freshEvents.addAll(events);
    }

    protected abstract void apply(GameEvent event);

    public Stream<GameEvent> freshEvents() {
        return freshEvents.stream();
    }

}
