package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckPileTest {

  @Test
  public void whenDrawFromEmptyDrawPileDiscardPileIsMovedIntoDrawPile() throws Exception {
    CardFactory cardFactory = new CardFactory();
    Deck deck = new Deck();
    List<Card> discardCards = List.of(cardFactory.card("refactor"),
                                      cardFactory.card("predict"),
                                      cardFactory.card("write code"));
    deck.addToDiscardPile(discardCards);

    List<Card> drawnCards = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      drawnCards.add(deck.draw());
    }

    assertThat(drawnCards)
      .containsExactlyInAnyOrderElementsOf(discardCards);
  }

  @Test
  public void whenDrawPileIsEmptyAndDiscardPileIsNotEmptyDrawPileSizeIsZero() throws Exception {
    CardFactory cardFactory = new CardFactory();
    Deck deck = new Deck();
    deck.addToDiscardPile(List.of(cardFactory.card("predict")));

    assertThat(deck.drawPileSize())
      .isZero();
  }
}
