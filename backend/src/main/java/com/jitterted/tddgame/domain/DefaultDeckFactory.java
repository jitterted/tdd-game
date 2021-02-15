package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;

public class DefaultDeckFactory implements DeckFactory {

  private final CardFactory cardFactory;

  public DefaultDeckFactory(CardFactory cardFactory) {
    this.cardFactory = cardFactory;
  }

  @Override
  public Deck<PlayingCard> createPlayingCardDeck() {
    Deck<PlayingCard> deck = new Deck<>(new RandomCardShuffler<>());
    addPlayingCardsToDiscardPileOf(deck);
    return deck;
  }

  @Override
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
    deck.addToDiscardPile(generatePlayingCards(18, PlayingCardDefinition.WRITE_CODE));
    deck.addToDiscardPile(generatePlayingCards(18, PlayingCardDefinition.LESS_CODE));
    deck.addToDiscardPile(generatePlayingCards(18, PlayingCardDefinition.PREDICT));
    deck.addToDiscardPile(generatePlayingCards(2, PlayingCardDefinition.CANT_ASSERT));
    deck.addToDiscardPile(generatePlayingCards(3, PlayingCardDefinition.CODE_BLOAT));
    deck.addToDiscardPile(generatePlayingCards(4, PlayingCardDefinition.REFACTOR));
  }

  private List<PlayingCard> generatePlayingCards(int count, PlayingCardDefinition playingCardDefinition) {
    List<PlayingCard> playingCards = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      playingCards.add(cardFactory.playingCard(playingCardDefinition));
    }
    return playingCards;
  }

}
