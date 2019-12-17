package com.jitterted.tddgame.domain;

public class CardNotInHandException extends RuntimeException {
  public CardNotInHandException(String message) {
    super(message);
  }
}
