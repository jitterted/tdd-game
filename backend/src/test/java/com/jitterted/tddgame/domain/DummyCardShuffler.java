package com.jitterted.tddgame.domain;

import java.util.List;

public class DummyCardShuffler<C> implements CardShuffler<C> {
  @Override
  public List<C> shuffle(List<C> cards) {
    throw new IllegalStateException("Shuffle should not have been called on a Dummy.");
  }
}
