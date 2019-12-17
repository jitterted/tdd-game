package com.jitterted.tddgame.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
  private final Map<PlayerId, Player> playerMap = new HashMap<>();
  private final Deck deck;

  public Game(List<Player> playerList, Deck deck) {
    playerList.forEach(player -> playerMap.put(player.id(), player));
    this.deck = deck;
  }

  public List<Player> players() {
    return new ArrayList<>(playerMap.values());
  }

  public Deck deck() {
    return deck;
  }

  public void start() {
    players().forEach(player -> player.fillHandFrom(deck));
  }

  public void discardFromHand(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.discardFromHand(cardId, deck);
  }

  public void discardFromInPlay(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.discardFromInPlay(cardId, deck);
  }

  public void drawCardFor(PlayerId playerId) {
    Player player = playerFor(playerId);
    player.drawFrom(deck);
  }

  public void playCardFor(PlayerId playerId, CardId cardId) {
    Player player = playerFor(playerId);
    player.play(cardId);
  }

  private Player playerFor(PlayerId playerId) {
    return playerMap.get(playerId);
  }
}
