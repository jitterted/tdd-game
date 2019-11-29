package com.jitterted.tddgame.domain;

import java.util.Collections;
import java.util.List;

public class RandomCardShuffler implements CardShuffler {
  @Override
  public List<Card> shuffle(List<Card> cards) {
    Collections.shuffle(cards);
    return cards;
  }
}
