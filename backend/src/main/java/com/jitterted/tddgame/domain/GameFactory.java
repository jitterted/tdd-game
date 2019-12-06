package com.jitterted.tddgame.domain;

import java.util.List;

public class GameFactory {

  private final DeckFactory deckFactory;
  private final PlayerFactory playerFactory;

  public GameFactory(DeckFactory deckFactory, PlayerFactory playerFactory) {
    this.deckFactory = deckFactory;
    this.playerFactory = playerFactory;
  }

  public Game createTwoPlayerGame() {
    List<Player> playerList = playerFactory.createTwoPlayers();
    Deck deck = deckFactory.createStandardDeck();
    return new Game(playerList, deck);
  }
}
