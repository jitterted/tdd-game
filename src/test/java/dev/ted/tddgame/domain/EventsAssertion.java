package dev.ted.tddgame.domain;

import org.assertj.core.api.Condition;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.Failures;

import java.util.List;
import java.util.stream.Stream;

class EventsAssertion {
    private final List<GameEvent> actualEvents;
    private final WritableAssertionInfo info = new WritableAssertionInfo();

    public EventsAssertion(Stream<GameEvent> actualEvents) {
        this.actualEvents = actualEvents.toList();
    }

    public EventsAssertion(List<GameEvent> events) {
        this.actualEvents = List.copyOf(events);
    }

    public void hasExactly(Class<?> clazz, int expectedCount) {
        Condition<Object> condition = new Condition<>(gameEvent -> gameEvent.getClass() == clazz,
                                                      "GameEvent is " + clazz.getSimpleName());
        int actualCount = Math.toIntExact(
                actualEvents.stream()
                            .filter(condition::matches)
                            .count());
        if (actualCount != expectedCount) {
            throw Failures
                    .instance()
                    .failure(info,
                             EventsShouldHaveExactly.eventsShouldHaveExactly(actualEvents, expectedCount, actualCount, condition));
        }

    }

}
