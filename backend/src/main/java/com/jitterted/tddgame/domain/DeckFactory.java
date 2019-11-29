package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;

public class DeckFactory {

  private final CardFactory cardFactory;

  public DeckFactory(CardFactory cardFactory) {
    this.cardFactory = cardFactory;
  }

  public Deck createStandardDeck() {
    Deck deck = new Deck(new RandomCardShuffler());
    addCardsToDiscardPileOf(deck);
    return deck;
  }

  private void addCardsToDiscardPileOf(Deck deck) {
    deck.addToDiscardPile(generateCards("write code", 18));
    deck.addToDiscardPile(generateCards("code smaller", 18));
    deck.addToDiscardPile(generateCards("predict", 18));
    deck.addToDiscardPile(generateCards("can't assert", 2));
    deck.addToDiscardPile(generateCards("code bloat", 3));
    deck.addToDiscardPile(generateCards("refactor", 4));
  }

  private List<Card> generateCards(String title, int count) {
    List<Card> cards = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      cards.add(cardFactory.card(title));
    }
    return cards;
  }

}
