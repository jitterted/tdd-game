package com.jitterted.tddgame.domain;

import java.util.List;

class CopyCardShuffler implements CardShuffler {
  @Override
  public List<Card> shuffle(List<Card> cards) {
    return cards;
  }
}
