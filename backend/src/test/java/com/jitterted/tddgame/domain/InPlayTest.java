package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InPlayTest {

  @Test
  public void inPlayAddTwoCardsAreReturned() throws Exception {
    CardFactory cardFactory = new CardFactory();
    Card card1 = cardFactory.card("one");
    Card card2 = cardFactory.card("two");

    InPlay inPlay = new InPlay();

    inPlay.add(card1);
    inPlay.add(card2);

    assertThat(inPlay.cards())
      .containsExactly(card1, card2);
  }
}
