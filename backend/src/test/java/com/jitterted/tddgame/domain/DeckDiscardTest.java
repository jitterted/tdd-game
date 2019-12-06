package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckDiscardTest {

  @Test
  public void newDeckHasEmptyDiscardPile() throws Exception {
    Deck deck = new Deck(null);

    assertThat(deck.discardPile())
      .isEmpty();
  }

  @Test
  public void discardCardToDeckResultsInCardInDiscardPile() throws Exception {
    Deck deck = new Deck(null);

    Card someCard = new Card(CardId.of(1), "some card");
    deck.addToDiscardPile(someCard);

    assertThat(deck.discardPile())
      .containsExactly(someCard);
  }
}
