package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Deck {
  private Queue<Card> drawPile = new LinkedList<>();
  private List<Card> discardPile = new ArrayList<>();

  Deck() {
  }

  public Card draw() {
    if (drawPile.isEmpty()) {
      replenishFromDiscard();
    }
    return drawPile.remove();
  }

  private void replenishFromDiscard() {
    drawPile.addAll(discardPile);
    discardPile.clear();
  }

  public void addToDrawPile(Card card) {
    drawPile.add(card);
  }

  public int drawPileSize() {
    return drawPile.size();
  }

  public void addToDiscardPile(List<Card> cards) {
    discardPile.addAll(cards);
  }
}
