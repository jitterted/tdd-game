package com.jitterted.tddgame.domain;

public class TestResultCardAlreadyDrawnException extends RuntimeException {
    public TestResultCardAlreadyDrawnException() {
        super();
    }

    public TestResultCardAlreadyDrawnException(String message) {
        super(message);
    }
}
