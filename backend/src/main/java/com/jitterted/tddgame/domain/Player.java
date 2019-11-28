package com.jitterted.tddgame.domain;

public class Player {

  private final Hand hand = new Hand();

  public Hand hand() {
    return hand;
  }

  public void drawFrom(Deck deck) {
    hand.add(deck.draw());
  }

  public void fillHandFrom(Deck deck) {
    while (hand.count() < 5) {
      drawFrom(deck);
    }
  }
}
