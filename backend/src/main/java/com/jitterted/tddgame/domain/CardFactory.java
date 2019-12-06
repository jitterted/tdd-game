package com.jitterted.tddgame.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class CardFactory {
  private final AtomicInteger idSequence = new AtomicInteger(0);

  public Card card(String title) {
    CardId cardId = new CardId(idSequence.getAndIncrement());
    return new Card(cardId, title);
  }
}
