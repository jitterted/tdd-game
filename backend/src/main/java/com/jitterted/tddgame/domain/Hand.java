package com.jitterted.tddgame.domain;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Hand {
  private final Map<CardId, PlayingCard> cards = new ConcurrentHashMap<>();
  private final PlayerId owningPlayerId; // only used by toString()

  public Hand(PlayerId playerId) {
    owningPlayerId = playerId;
  }

  public boolean isEmpty() {
    return cards.isEmpty();
  }

  public boolean contains(PlayingCard playingCard) {
    return cards.containsKey(playingCard.id());
  }

  public void add(PlayingCard playingCard) {
    cards.put(playingCard.id(), playingCard);
  }

  public int count() {
    return cards.size();
  }

  public List<PlayingCard> cards() {
    return List.copyOf(cards.values());
  }

  public PlayingCard remove(CardId cardId) {
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
