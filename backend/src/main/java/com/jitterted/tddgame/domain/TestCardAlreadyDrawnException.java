package com.jitterted.tddgame.domain;

public class TestCardAlreadyDrawnException extends RuntimeException {
  public TestCardAlreadyDrawnException() {
    super();
  }

  public TestCardAlreadyDrawnException(String message) {
    super(message);
  }
}
