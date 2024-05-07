package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InPlay {
    private final Map<CardId, PlayingCard> cards = new HashMap<>();

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean contains(PlayingCard playingCard) {
        return cards.containsKey(playingCard.id());
    }

    public void add(PlayingCard playingCard) {
        cards.put(playingCard.id(), playingCard);
    }

    public List<PlayingCard> cards() {
        return new ArrayList<>(cards.values());
    }

    public PlayingCard remove(CardId cardId) {
        if (cards.containsKey(cardId)) {
            return cards.remove(cardId);
        }
        throw new CardNotInPlayException("Could not remove card " + cardId + ", it's not in-play.");
    }
}
