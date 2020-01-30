package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardFactoryTest {
  @Test
  public void twoPlayingCardInstancesWithSameTitleAreNotEqual() throws Exception {
    CardFactory cardFactory = new CardFactory();
    PlayingCard refactor1 = cardFactory.playingCard("refactor", Usage.SELF);
    PlayingCard refactor2 = cardFactory.playingCard("refactor", Usage.SELF);

    assertThat(refactor1)
      .isNotEqualTo(refactor2);
  }
}
