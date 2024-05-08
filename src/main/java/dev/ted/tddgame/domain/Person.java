package dev.ted.tddgame.domain;

public class Person {
    private final Long id;

    public Person(long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }
}
