package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class DeckDrawPileReplenishTest {

  @Test
  public void whenDrawFromEmptyDrawPileDiscardPileIsMovedIntoDrawPile() throws Exception {
    Deck deck = new Deck(new CopyCardShuffler());
    List<PlayingCard> discardedPlayingCards = discardThreeCardsTo(deck);

    List<PlayingCard> drawnPlayingCards = drawThreeCardsFrom(deck);

    assertThat(drawnPlayingCards)
      .containsExactlyInAnyOrderElementsOf(discardedPlayingCards);
  }

  @Test
  public void whenDrawPileIsEmptyAndDiscardPileIsNotEmptyDrawPileSizeIsZero() throws Exception {
    CardFactory cardFactory = new CardFactory();
    Deck deck = new Deck(new CopyCardShuffler());
    deck.addToDiscardPile(List.of(cardFactory.playingCard("predict", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF)));

    assertThat(deck.drawPileSize())
      .isZero();
  }

  @Test
  public void drawPileIsReplenishedFromShuffledDiscardPile() throws Exception {
    CardShuffler<PlayingCard> shuffler = new ReverseCardShuffler<>();
    Deck<PlayingCard> deck = new Deck<>(shuffler);
    List<PlayingCard> discardedPlayingCards = discardThreeCardsTo(deck);

    List<PlayingCard> drawnPlayingCards = drawThreeCardsFrom(deck);

    assertThat(drawnPlayingCards)
      .doesNotContainSequence(discardedPlayingCards);
  }

  private List<PlayingCard> discardThreeCardsTo(Deck<PlayingCard> deck) {
    CardFactory cardFactory = new CardFactory();
    List<PlayingCard> discardPlayingCards = List.of(cardFactory.playingCard("refactor", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF),
                                                    cardFactory.playingCard("predict", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF),
                                                    cardFactory.playingCard("write code", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF));
    deck.addToDiscardPile(discardPlayingCards);
    return discardPlayingCards;
  }

  private List<PlayingCard> drawThreeCardsFrom(Deck<PlayingCard> deck) {
    List<PlayingCard> drawnPlayingCards = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      drawnPlayingCards.add(deck.draw());
    }
    return drawnPlayingCards;
  }


  private static class ReverseCardShuffler<C> implements CardShuffler<C> {
    @Override
    public List<C> shuffle(List<C> cards) {
      Collections.reverse(cards);
      return cards;
    }
  }
}
