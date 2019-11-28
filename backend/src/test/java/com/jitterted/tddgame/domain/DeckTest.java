package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

  @Test
  public void drawOneCardFromDeckOfOneCardReturnsGivenCard() throws Exception {
    Card card = new Card("Write Code");
    Deck deck = new Deck();
    deck.add(card);

    assertThat(deck.draw())
      .isEqualTo(card);
  }

  @Test
  public void drawTwoCardsReturnsTwoCardsFromDeck() throws Exception {
    Deck deck = new Deck();
    Card card1 = new Card("One");
    Card card2 = new Card("Two");
    deck.add(card1);
    deck.add(card2);

    List<Card> drawnCards = List.of(deck.draw(), deck.draw());

    assertThat(drawnCards)
      .containsExactlyInAnyOrder(card1, card2);
  }

  @Test
  public void drawOneCardFromDeckOfThreeLeavesDeckWithTwoCards() throws Exception {
    Deck deck = new Deck();
    deck.add(new Card("One"));
    deck.add(new Card("Two"));
    deck.add(new Card("Three"));

    deck.draw();

    assertThat(deck.size())
      .isEqualTo(2);
  }

}
