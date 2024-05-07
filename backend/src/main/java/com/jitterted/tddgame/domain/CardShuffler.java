package com.jitterted.tddgame.domain;

import java.util.List;

public interface CardShuffler<C> {
    List<C> shuffle(List<C> cards);
}
