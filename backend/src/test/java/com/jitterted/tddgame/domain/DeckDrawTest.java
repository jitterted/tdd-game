package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class DeckDrawTest {

  private final CardFactory cardFactory = new CardFactory();

  @Test
  public void drawOneCardFromDeckOfOneCardReturnsGivenCard() throws Exception {
    Card card = cardFactory.card("Write Code", Usage.SELF);
    Deck<Card> deck = new Deck<>(null);
    deck.addToDrawPile(card);

    assertThat(deck.draw())
      .isEqualTo(card);
  }

  @Test
  public void drawTwoCardsReturnsTwoCardsFromDeck() throws Exception {
    Deck<Card> deck = new Deck<>(null);
    Card card1 = cardFactory.card("One", Usage.SELF);
    Card card2 = cardFactory.card("Two", Usage.SELF);
    deck.addToDrawPile(card1);
    deck.addToDrawPile(card2);

    List<Card> drawnCards = List.of(deck.draw(), deck.draw());

    assertThat(drawnCards)
      .containsExactlyInAnyOrder(card1, card2);
  }

  @Test
  public void drawOneCardFromDeckOfThreeLeavesDeckWithTwoCards() throws Exception {
    Deck<Card> deck = new Deck<>(null);
    deck.addToDrawPile(cardFactory.card("One", Usage.SELF));
    deck.addToDrawPile(cardFactory.card("Two", Usage.SELF));
    deck.addToDrawPile(cardFactory.card("Three", Usage.SELF));

    deck.draw();

    assertThat(deck.drawPileSize())
      .isEqualTo(2);
  }

}
