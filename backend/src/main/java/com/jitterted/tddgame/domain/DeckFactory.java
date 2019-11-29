package com.jitterted.tddgame.domain;

public class DeckFactory {

  private final CardFactory cardFactory;

  public DeckFactory(CardFactory cardFactory) {
    this.cardFactory = cardFactory;
  }

  public Deck createStandardDeck() {
    Deck deck = new Deck();
    addCardsTo(deck);
    return deck;
  }

  private void addCardsTo(Deck deck) {
    addCards(deck, "write code", 18);
    addCards(deck, "code smaller", 18);
    addCards(deck, "predict", 18);
    addCards(deck, "can't assert", 2);
    addCards(deck, "code bloat", 3);
    addCards(deck, "refactor", 4);
  }

  private void addCards(Deck deck, String title, int count) {
    for (int i = 0; i < count; i++) {
      deck.add(cardFactory.card(title));
    }
  }

}
