package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;

public class DeckFactory {

  private final CardFactory cardFactory;

  public DeckFactory(CardFactory cardFactory) {
    this.cardFactory = cardFactory;
  }

  public Deck<PlayingCard> createPlayingCardDeck() {
    Deck<PlayingCard> deck = new Deck<>(new RandomCardShuffler<>());
    addPlayingCardsToDiscardPileOf(deck);
    return deck;
  }

  public Deck<TestResultCard> createTestResultCardDeck() {
    Deck<TestResultCard> testResultCardDeck = new Deck<>(new RandomCardShuffler<>());
    testResultCardDeck.addToDiscardPile(generateTestResultCards("as predicted", 3));
    testResultCardDeck.addToDiscardPile(generateTestResultCards("require 1", 3));
    testResultCardDeck.addToDiscardPile(generateTestResultCards("require 2", 3));
    return testResultCardDeck;
  }

  private List<TestResultCard> generateTestResultCards(String title, int count) {
    List<TestResultCard> cards = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      cards.add(cardFactory.testResultCard(title));
    }
    return cards;
  }


  private void addPlayingCardsToDiscardPileOf(Deck<PlayingCard> deck) {
    deck.addToDiscardPile(generatePlayingCards("write code", 18, OnDrawGoesTo.HAND, OnPlayGoesTo.SELF));
    deck.addToDiscardPile(generatePlayingCards("less code", 18, OnDrawGoesTo.HAND, OnPlayGoesTo.SELF));
    deck.addToDiscardPile(generatePlayingCards("predict", 18, OnDrawGoesTo.HAND, OnPlayGoesTo.SELF));
    deck.addToDiscardPile(generatePlayingCards("can't assert", 2, OnDrawGoesTo.IN_PLAY, OnPlayGoesTo.SELF));
    deck.addToDiscardPile(generatePlayingCards("code bloat", 3, OnDrawGoesTo.IN_PLAY, OnPlayGoesTo.SELF));
    deck.addToDiscardPile(generatePlayingCards("refactor", 4, OnDrawGoesTo.HAND, OnPlayGoesTo.DISCARD));
  }

  private List<PlayingCard> generatePlayingCards(String title, int count, OnDrawGoesTo onDrawGoesTo, OnPlayGoesTo onPlayGoesTo) {
    List<PlayingCard> playingCards = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      playingCards.add(cardFactory.playingCard(title, onPlayGoesTo, onDrawGoesTo));
    }
    return playingCards;
  }
}
