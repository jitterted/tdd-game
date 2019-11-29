package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

  @Test
  public void drawOneCardFromDeckOfOneCardReturnsGivenCard() throws Exception {
    Card card = new Card(0, "Write Code");
    Deck deck = new Deck();
    deck.addToDrawPile(card);

    assertThat(deck.draw())
      .isEqualTo(card);
  }

  @Test
  public void drawTwoCardsReturnsTwoCardsFromDeck() throws Exception {
    Deck deck = new Deck();
    Card card1 = new Card(1, "One");
    Card card2 = new Card(2, "Two");
    deck.addToDrawPile(card1);
    deck.addToDrawPile(card2);

    List<Card> drawnCards = List.of(deck.draw(), deck.draw());

    assertThat(drawnCards)
      .containsExactlyInAnyOrder(card1, card2);
  }

  @Test
  public void drawOneCardFromDeckOfThreeLeavesDeckWithTwoCards() throws Exception {
    Deck deck = new Deck();
    deck.addToDrawPile(new Card(1, "One"));
    deck.addToDrawPile(new Card(2, "Two"));
    deck.addToDrawPile(new Card(3, "Three"));

    deck.draw();

    assertThat(deck.drawPileSize())
      .isEqualTo(2);
  }

}
