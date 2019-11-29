package com.jitterted.tddgame.domain;

import java.util.List;

public interface CardShuffler {
  List<Card> shuffle(List<Card> cards);
}
