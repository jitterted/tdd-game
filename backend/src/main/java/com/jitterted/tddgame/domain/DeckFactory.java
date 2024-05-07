package com.jitterted.tddgame.domain;

public interface DeckFactory {
    Deck<PlayingCard> createPlayingCardDeck();

    Deck<TestResultCard> createTestResultCardDeck();
}
