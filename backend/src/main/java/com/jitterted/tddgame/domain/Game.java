package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
  private final Map<PlayerId, Player> playerMap = new HashMap<>();
  private final Deck<PlayingCard> playingCardDeck;
  private final Deck<TestResultCard> testResultCardDeck;

  public Game(List<Player> playerList, Deck<PlayingCard> playingCardDeck, Deck<TestResultCard> testResultCardDeck) {
    playerList.forEach(player -> playerMap.put(player.id(), player));
    this.playingCardDeck = playingCardDeck;
    this.testResultCardDeck = testResultCardDeck;
  }

  public List<Player> players() {
    return new ArrayList<>(playerMap.values());
  }

  public Deck<PlayingCard> deck() {
    return playingCardDeck;
  }

  public void start() {
    players().forEach(player -> player.fillHandFrom(playingCardDeck));
  }

  public void discardFromHand(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.discardFromHand(cardId, playingCardDeck);
  }

  public void discardFromInPlay(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.discardFromInPlay(cardId, playingCardDeck);
  }

  public void drawCardFor(PlayerId playerId) {
    Player player = playerFor(playerId);
    player.drawFrom(playingCardDeck);
  }

  public void playCardFor(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.play(this, cardId);
  }

  public TestResultCard drawTestResultCardFor(PlayerId playerId) {
    TestResultCard drawnCard = testResultCardDeck.draw();
    testResultCardDeck.addToDiscardPile(drawnCard);
    return drawnCard;
  }

  public Player playerFor(PlayerId playerId) {
    return playerMap.get(playerId);
  }

  public Player opponentFor(Player player) {
    return playerMap.values()
                    .stream()
                    .filter(p -> !p.equals(player))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No opponent found for " + player));
  }
}
