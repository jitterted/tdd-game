package com.jitterted.tddgame.domain;

import java.util.LinkedList;
import java.util.Queue;

public class Deck {
  private Queue<Card> cardQueue = new LinkedList<>();

  Deck() {
  }

  public Card draw() {
    return cardQueue.remove();
  }

  public void add(Card card) {
    cardQueue.add(card);
  }

  public int size() {
    return cardQueue.size();
  }
}
