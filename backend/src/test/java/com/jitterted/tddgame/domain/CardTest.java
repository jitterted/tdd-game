package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardTest {
  @Test
  public void twoCardInstancesWithSameTitleAreNotEqual() throws Exception {
    CardFactory cardFactory = new CardFactory();
    Card refactor1 = cardFactory.card("Refactor", Usage.SELF);
    Card refactor2 = cardFactory.card("Refactor", Usage.SELF);

    assertThat(refactor1)
      .isNotEqualTo(refactor2);
  }
}
