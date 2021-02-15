package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

public class CreateDeckTest {

  @Test
  public void newDeckHasFullSetOfCardsAfterShuffleTriggeredByDraw() throws Exception {
    Deck<PlayingCard> deck = new DefaultDeckFactory(new CardFactory()).createPlayingCardDeck();

    deck.draw(); // trigger shuffle from discard over to draw pile and draw 1

    assertThat(deck.drawPileSize())
      .isEqualTo(62); // 62 cards left (of 63)
  }

  @Test
  public void newDeckAllowsDrawUpToOnlyFullSetOfCards() throws Exception {
    Deck<PlayingCard> deck = new DefaultDeckFactory(new CardFactory()).createPlayingCardDeck();

    for (int i = 0; i < 63; i++) {
      deck.draw();
    }

    assertThatThrownBy(deck::draw)
      .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  public void testResultCardDeckHas3ofEachTypeOfCard() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new DefaultDeckFactory(new CardFactory()).createTestResultCardDeck();

    testResultCardDeck.draw(); // trigger shuffle from discard over to draw pile and draw 1

    assertThat(testResultCardDeck.drawPileSize()) // should be 8 remaining
      .isEqualTo(8);
  }
}
