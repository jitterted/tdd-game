package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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

    PlayingCard somePlayingCard = new CardFactory().playingCard("some card", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF);
    deck.addToDiscardPile(somePlayingCard);

    assertThat(deck.discardPile())
      .containsExactly(somePlayingCard);
  }
}
