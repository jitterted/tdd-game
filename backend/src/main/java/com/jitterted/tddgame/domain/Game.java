package com.jitterted.tddgame.domain;

import java.util.List;

public class Game {
  private final List<Player> playerList;
  private final Deck deck;

  public Game(List<Player> playerList, Deck deck) {
    this.playerList = playerList;
    this.deck = deck;
  }

  public List<Player> players() {
    return playerList;
  }

  public Deck deck() {
    return deck;
  }

  public void start() {
    playerList.forEach(player -> player.fillHandFrom(deck));
  }
}
