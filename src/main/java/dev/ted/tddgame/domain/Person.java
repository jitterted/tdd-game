package dev.ted.tddgame.domain;

public class Person {
    private final PersonId personId;

    public Person(PersonId personId) {
        this.personId = personId;
    }

    public PersonId id() {
        return personId;
    }
}
