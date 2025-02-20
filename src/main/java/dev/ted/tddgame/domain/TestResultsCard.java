package dev.ted.tddgame.domain;

public enum TestResultsCard implements Card {
    AS_PREDICTED("As Predicted")
    , NEED_ONE_LESS_CODE("Need At Least One LESS CODE card")
    , NEED_TWO_LESS_CODE("Need At Least Two LESS CODE cards");

    private final String title;

    TestResultsCard(String title) {
        this.title = title;
    }

    @Override
    public String title() {
        return title;
    }
}
