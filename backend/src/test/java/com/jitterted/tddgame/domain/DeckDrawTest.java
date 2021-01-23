package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class DeckDrawTest {

  private final CardFactory cardFactory = new CardFactory();

  @Test
  public void drawOneCardFromDeckOfOneCardReturnsGivenCard() throws Exception {
    PlayingCard playingCard = cardFactory.playingCard("Write Code", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    Deck<PlayingCard> deck = new Deck<>(null);
    deck.addToDrawPile(playingCard);

    assertThat(deck.draw())
      .isEqualTo(playingCard);
  }

  @Test
  public void drawTwoCardsReturnsTwoCardsFromDeck() throws Exception {
    Deck<PlayingCard> deck = new Deck<>(null);
    PlayingCard playingCard1 = cardFactory.playingCard("One", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    PlayingCard playingCard2 = cardFactory.playingCard("Two", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    deck.addToDrawPile(playingCard1);
    deck.addToDrawPile(playingCard2);

    List<PlayingCard> drawnPlayingCards = List.of(deck.draw(), deck.draw());

    assertThat(drawnPlayingCards)
      .containsExactlyInAnyOrder(playingCard1, playingCard2);
  }

  @Test
  public void drawOneCardFromDeckOfThreeLeavesDeckWithTwoCards() throws Exception {
    Deck<PlayingCard> deck = new Deck<>(null);
    deck.addToDrawPile(cardFactory.playingCard("One", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    deck.addToDrawPile(cardFactory.playingCard("Two", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    deck.addToDrawPile(cardFactory.playingCard("Three", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));

    deck.draw();

    assertThat(deck.drawPileSize())
      .isEqualTo(2);
  }

}
