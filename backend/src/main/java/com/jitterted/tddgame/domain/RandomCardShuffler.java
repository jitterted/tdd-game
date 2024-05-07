package com.jitterted.tddgame.domain;

import java.util.Collections;
import java.util.List;

public class RandomCardShuffler<C> implements CardShuffler<C> {
    @Override
    public List<C> shuffle(List<C> cards) {
        Collections.shuffle(cards);
        return cards;
    }
}
