package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HandTest {

  private CardFactory cardFactory = new CardFactory();

  @Test
  public void handIsEmptyWhenNotHoldingAnyCards() throws Exception {
    Hand hand = new Hand(PlayerId.of(0));

    assertThat(hand.isEmpty())
      .isTrue();
  }

  @Test
  public void handIsNotFullWhenHoldingFourCards() throws Exception {
    Hand hand = new Hand(PlayerId.of(0));

    hand.add(cardFactory.card("one", Usage.SELF));
    hand.add(cardFactory.card("two", Usage.SELF));
    hand.add(cardFactory.card("three", Usage.SELF));
    hand.add(cardFactory.card("four", Usage.SELF));

    assertThat(hand.isFull())
      .isFalse();
  }

  @Test
  public void handIsFullWhenHoldingFiveCards() throws Exception {
    Hand hand = new Hand(PlayerId.of(0));

    hand.add(cardFactory.card("one", Usage.SELF));
    hand.add(cardFactory.card("two", Usage.SELF));
    hand.add(cardFactory.card("three", Usage.SELF));
    hand.add(cardFactory.card("four", Usage.SELF));
    hand.add(cardFactory.card("five", Usage.SELF));

    assertThat(hand.isFull())
      .isTrue();
  }

}
