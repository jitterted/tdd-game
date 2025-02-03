package dev.ted.tddgame.domain;

public class CanNotPlayCardHereException extends RuntimeException {
    public CanNotPlayCardHereException() {
        super();
    }

    public CanNotPlayCardHereException(String message) {
        super(message);
    }
}
