package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateDeckTest {

  @Test
  public void newDeckHasFullSetOfCards() throws Exception {
    Deck deck = new DeckFactory(new CardFactory()).createStandardDeck();

    assertThat(deck.drawPileSize())
      .isEqualTo(63);
  }
}
