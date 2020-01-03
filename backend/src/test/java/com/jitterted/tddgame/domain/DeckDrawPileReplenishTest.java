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
    List<Card> discardedCards = discardThreeCardsTo(deck);

    List<Card> drawnCards = drawThreeCardsFrom(deck);

    assertThat(drawnCards)
      .containsExactlyInAnyOrderElementsOf(discardedCards);
  }

  @Test
  public void whenDrawPileIsEmptyAndDiscardPileIsNotEmptyDrawPileSizeIsZero() throws Exception {
    CardFactory cardFactory = new CardFactory();
    Deck deck = new Deck(new CopyCardShuffler());
    deck.addToDiscardPile(List.of(cardFactory.card("predict", Usage.SELF)));

    assertThat(deck.drawPileSize())
      .isZero();
  }

  @Test
  public void drawPileIsReplenishedFromShuffledDiscardPile() throws Exception {
    CardShuffler<Card> shuffler = new ReverseCardShuffler<>();
    Deck<Card> deck = new Deck<>(shuffler);
    List<Card> discardedCards = discardThreeCardsTo(deck);

    List<Card> drawnCards = drawThreeCardsFrom(deck);

    assertThat(drawnCards)
      .doesNotContainSequence(discardedCards);
  }

  private List<Card> discardThreeCardsTo(Deck<Card> deck) {
    CardFactory cardFactory = new CardFactory();
    List<Card> discardCards = List.of(cardFactory.card("refactor", Usage.SELF),
                                      cardFactory.card("predict", Usage.SELF),
                                      cardFactory.card("write code", Usage.SELF));
    deck.addToDiscardPile(discardCards);
    return discardCards;
  }

  private List<Card> drawThreeCardsFrom(Deck<Card> deck) {
    List<Card> drawnCards = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      drawnCards.add(deck.draw());
    }
    return drawnCards;
  }


  private static class ReverseCardShuffler<C> implements CardShuffler<C> {
    @Override
    public List<C> shuffle(List<C> cards) {
      Collections.reverse(cards);
      return cards;
    }
  }
}
