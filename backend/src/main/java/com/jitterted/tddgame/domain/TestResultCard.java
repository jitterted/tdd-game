package com.jitterted.tddgame.domain;

import org.springframework.lang.NonNull;

public class TestResultCard {
    private final CardId id;
    private final String title;

    public TestResultCard(@NonNull CardId id, @NonNull String title) {
        this.id = id;
        this.title = title;
    }

    public CardId id() {
        return id;
    }

    public String title() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestResultCard that = (TestResultCard) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TestResultCard (" + id.getId() + "): \"" + title + "\"";
    }

}
