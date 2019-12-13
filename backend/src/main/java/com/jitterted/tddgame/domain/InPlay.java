package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InPlay {
  private final Map<CardId, Card> cards = new HashMap<>();

  public List<Card> cards() {
    return new ArrayList<>(cards.values());
  }

  public boolean isEmpty() {
    return cards.isEmpty();
  }

  public void add(Card card) {
    cards.put(card.id(), card);
  }

  public boolean contains(Card card) {
    return cards.containsKey(card.id());
  }
}
