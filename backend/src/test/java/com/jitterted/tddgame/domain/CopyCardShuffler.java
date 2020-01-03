package com.jitterted.tddgame.domain;

import java.util.List;

class CopyCardShuffler<C> implements CardShuffler<C> {
  @Override
  public List<C> shuffle(List<C> cards) {
    return cards;
  }
}
