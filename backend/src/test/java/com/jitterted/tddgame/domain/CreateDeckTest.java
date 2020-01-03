package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

public class CreateDeckTest {

  @Test
  public void newDeckHasFullSetOfCardsAfterShuffleTriggeredByDraw() throws Exception {
    Deck deck = new DeckFactory(new CardFactory()).createPlayingCardDeck();

    deck.draw(); // trigger shuffle from discard over to draw pile and draw 1

    assertThat(deck.drawPileSize())
      .isEqualTo(62); // 62 cards left (of 63)
  }

  @Test
  public void newDeckAllowsDrawUpToOnlyFullSetOfCards() throws Exception {
    Deck deck = new DeckFactory(new CardFactory()).createPlayingCardDeck();

    for (int i = 0; i < 63; i++) {
      deck.draw();
    }

    assertThatThrownBy(deck::draw)
      .isInstanceOf(NoSuchElementException.class);
  }
}
