package com.jitterted.tddgame.domain;

public class WrongPlayerDiscardingTestResultCardException extends RuntimeException {
  public WrongPlayerDiscardingTestResultCardException(PlayerId playerAttemptingDiscard,
                                                      Player playerAllowedToDiscard) {
    this("Player "
           + playerAttemptingDiscard
           + " attempted to discard Test Result Card drawn by "
           + playerAllowedToDiscard);
  }

  public WrongPlayerDiscardingTestResultCardException(String message) {
    super(message);
  }
}
