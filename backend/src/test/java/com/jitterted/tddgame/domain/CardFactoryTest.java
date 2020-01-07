package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CardFactoryTest {
  @Test
  public void twoPlayingCardInstancesWithSameTitleAreNotEqual() throws Exception {
    CardFactory cardFactory = new CardFactory();
    PlayingCard refactor1 = cardFactory.playingCard("Refactor", Usage.SELF);
    PlayingCard refactor2 = cardFactory.playingCard("Refactor", Usage.SELF);

    assertThat(refactor1)
      .isNotEqualTo(refactor2);
  }
}
