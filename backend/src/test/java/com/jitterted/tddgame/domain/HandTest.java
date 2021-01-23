package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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

    hand.add(cardFactory.playingCard("one", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    hand.add(cardFactory.playingCard("two", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    hand.add(cardFactory.playingCard("three", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    hand.add(cardFactory.playingCard("four", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));

    assertThat(hand.isFull())
      .isFalse();
  }

  @Test
  public void handIsFullWhenHoldingFiveCards() throws Exception {
    Hand hand = new Hand(PlayerId.of(0));

    hand.add(cardFactory.playingCard("one", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    hand.add(cardFactory.playingCard("two", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    hand.add(cardFactory.playingCard("three", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    hand.add(cardFactory.playingCard("four", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));
    hand.add(cardFactory.playingCard("five", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND));

    assertThat(hand.isFull())
      .isTrue();
  }

}
