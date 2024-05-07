package com.jitterted.tddgame.domain;

public class CardNotInPlayException extends RuntimeException {
    public CardNotInPlayException(String message) {
        super(message);
    }
}
