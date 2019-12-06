package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hand {
  private final Map<CardId, Card> cards = new HashMap<>();
  private final PlayerId owningPlayerId;

  public Hand(PlayerId playerId) {
    owningPlayerId = playerId;
  }

  public boolean isEmpty() {
    return cards.isEmpty();
  }

  public boolean contains(Card card) {
    return cards.containsKey(card.id());
  }

  public void add(Card card) {
    cards.put(card.id(), card);
  }

  public int count() {
    return cards.size();
  }

  public List<Card> cards() {
    return new ArrayList<>(cards.values());
  }

  public Card remove(CardId cardId) {
    if (cards.containsKey(cardId)) {
      return cards.remove(cardId);
    }
    throw new CardNotInHandException("Could not remove card " + cardId + ": not in hand (" + this + ")");
  }

  public boolean isFull() {
    return cards.size() == 5;
  }

  @Override
  public String toString() {
    return "Hand: " + cards.values() + ", owner = " + owningPlayerId;
  }
}
