package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

  @Test
  public void drawOneCardFromDeckOfOneCardReturnsGivenCard() throws Exception {
    Card card = new Card(0, "Write Code");
    Deck deck = new Deck();
    deck.add(card);

    assertThat(deck.draw())
      .isEqualTo(card);
  }

  @Test
  public void drawTwoCardsReturnsTwoCardsFromDeck() throws Exception {
    Deck deck = new Deck();
    Card card1 = new Card(1, "One");
    Card card2 = new Card(2, "Two");
    deck.add(card1);
    deck.add(card2);

    List<Card> drawnCards = List.of(deck.draw(), deck.draw());

    assertThat(drawnCards)
      .containsExactlyInAnyOrder(card1, card2);
  }

  @Test
  public void drawOneCardFromDeckOfThreeLeavesDeckWithTwoCards() throws Exception {
    Deck deck = new Deck();
    deck.add(new Card(1, "One"));
    deck.add(new Card(2, "Two"));
    deck.add(new Card(3, "Three"));

    deck.draw();

    assertThat(deck.size())
      .isEqualTo(2);
  }

}
