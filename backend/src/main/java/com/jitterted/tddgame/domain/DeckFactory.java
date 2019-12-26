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
    deck.addToDiscardPile(generateCards("write code", 18, Usage.SELF));
    deck.addToDiscardPile(generateCards("code smaller", 18, Usage.SELF));
    deck.addToDiscardPile(generateCards("predict", 18, Usage.SELF));
    deck.addToDiscardPile(generateCards("can't assert", 2, Usage.OPPONENT));
    deck.addToDiscardPile(generateCards("code bloat", 3, Usage.OPPONENT));
    deck.addToDiscardPile(generateCards("refactor", 4, Usage.DISCARD));
  }

  private List<Card> generateCards(String title, int count, Usage usage) {
    List<Card> cards = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      cards.add(cardFactory.card(title, usage));
    }
    return cards;
  }

}
