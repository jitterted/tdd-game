package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class EventSourcedAggregate {

    protected List<GameEvent> freshEvents = new ArrayList<>();

    protected void enqueue(GameEvent event) {
        freshEvents.add(event);
        apply(event);
    }

    protected abstract void apply(GameEvent event);

    public Stream<GameEvent> freshEvents() {
        return freshEvents.stream();
    }

}
