package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;

public class Hand {
  private final List<Card> cards = new ArrayList<>();

  public boolean isEmpty() {
    return cards.isEmpty();
  }

  public boolean contains(Card card) {
    return cards.contains(card);
  }

  public void add(Card card) {
    cards.add(card);
  }

  public int count() {
    return cards.size();
  }
}
