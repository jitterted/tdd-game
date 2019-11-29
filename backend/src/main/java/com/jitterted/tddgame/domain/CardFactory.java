package com.jitterted.tddgame.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class CardFactory {
  private final AtomicInteger idSequence = new AtomicInteger(0);

  public Card card(String title) {
    return new Card(idSequence.getAndIncrement(), title);
  }
}
