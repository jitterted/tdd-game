package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InPlay {
  private final Map<CardId, Card> cards = new HashMap<>();

  public boolean isEmpty() {
    return cards.isEmpty();
  }

  public boolean contains(Card card) {
    return cards.containsKey(card.id());
  }

  public void add(Card card) {
    cards.put(card.id(), card);
  }

  public List<Card> cards() {
    return new ArrayList<>(cards.values());
  }

  public Card remove(CardId cardId) {
    if (cards.containsKey(cardId)) {
      return cards.remove(cardId);
    }
    throw new CardNotInPlayException("Could not remove card " + cardId + ", it's not in-play.");
  }
}
