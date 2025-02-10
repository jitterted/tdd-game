package dev.ted.tddgame.domain;

public class HandAlreadyFull extends RuntimeException {

    public HandAlreadyFull(String message) {
        super(message);
    }

}
