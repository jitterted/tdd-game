package com.jitterted.tddgame.domain;

public class CardNotInHandException extends RuntimeException {
  public CardNotInHandException() {
    super();
  }

  public CardNotInHandException(String message) {
    super(message);
  }
}
