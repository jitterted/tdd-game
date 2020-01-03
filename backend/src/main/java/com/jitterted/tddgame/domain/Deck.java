package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Deck<C> {
  private Queue<C> drawPile = new LinkedList<>();
  private List<C> discardPile = new ArrayList<>();
  private final CardShuffler<C> shuffler;

  Deck(CardShuffler<C> shuffler) {
    this.shuffler = shuffler;
  }

  public C draw() {
    if (drawPile.isEmpty()) {
      replenishFromDiscard();
    }
    return drawPile.remove();
  }

  private void replenishFromDiscard() {
    drawPile.addAll(shuffler.shuffle(discardPile));
    discardPile.clear();
  }

  public void addToDrawPile(C card) {
    drawPile.add(card);
  }

  public int drawPileSize() {
    return drawPile.size();
  }

  public void addToDiscardPile(C card) {
    discardPile.add(card);
  }

  public void addToDiscardPile(List<C> cards) {
    discardPile.addAll(cards);
  }

  public List<C> discardPile() {
    return Collections.unmodifiableList(discardPile);
  }
}
