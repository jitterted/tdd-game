package com.jitterted.tddgame.domain;

import com.jitterted.tddgame.domain.port.GameStateChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
  private final Map<PlayerId, Player> playerMap = new HashMap<>();
  private final Deck<PlayingCard> playingCardDeck;
  private final Deck<TestResultCard> testResultCardDeck;
  private final GameStateChannel gameStateChannel;
  private DrawnTestResultCard drawnTestResultCard;

  // GOAL: remove this constructor in favor of the one taking the GameStateChannel
  public Game(List<Player> playerList,
              Deck<PlayingCard> playingCardDeck,
              Deck<TestResultCard> testResultCardDeck) {
    playerList.forEach(player -> playerMap.put(player.id(), player));
    this.playingCardDeck = playingCardDeck;
    this.testResultCardDeck = testResultCardDeck;
    this.gameStateChannel = new GameStateChannel() {
      @Override public void testResultCardDrawn(DrawnTestResultCard drawnTestResultCard) { }
      @Override public void testResultCardDiscarded(PlayerId playerId) { }
      @Override public void playerActed(Game game) { }
    };
  }

  public Game(List<Player> playerList, Deck<PlayingCard> playingCardDeck, Deck<TestResultCard> testResultCardDeck, GameStateChannel gameStateChannel) {
    playerList.forEach(player -> playerMap.put(player.id(), player));
    this.playingCardDeck = playingCardDeck;
    this.testResultCardDeck = testResultCardDeck;
    this.gameStateChannel = gameStateChannel;
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
    gameStateChannel.playerActed(this);
  }

  public void discardFromInPlay(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.discardFromInPlay(cardId, playingCardDeck);
  }

  public void drawCardFor(PlayerId playerId) {
    Player player = playerFor(playerId);
    player.drawFrom(playingCardDeck);
  }

  public void drawToFullHandFor(PlayerId playerId) {
    Player player = playerFor(playerId);
    player.fillHandFrom(playingCardDeck);
  }

  public void playCardFor(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.play(this, cardId);
  }

  public void drawTestResultCardFor(PlayerId playerId) {
    if (drawnTestResultCard != null) {
      throw new TestResultCardAlreadyDrawnException("Currently drawn card was not discarded: " + drawnTestResultCard);
    }
    TestResultCard drawnCard = testResultCardDeck.draw();
    drawnTestResultCard = new DrawnTestResultCard(drawnCard, playerFor(playerId));
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

  public Deck<TestResultCard> testResultCardDeck() {
    return testResultCardDeck;
  }

  public DrawnTestResultCard drawnTestResultCard() {
    return drawnTestResultCard;
  }

  public void discardTestResultCardFor(PlayerId playerId) {
    if (!drawnTestResultCard.discardableBy(playerId)) {
      throw new WrongPlayerDiscardingTestResultCardException(playerId, drawnTestResultCard.player());
    }
    testResultCardDeck.addToDiscardPile(drawnTestResultCard.card());
    drawnTestResultCard = null;
  }

  public Location locationFor(PlayerId playerId) {
    return Location.ONE;
  }
}
