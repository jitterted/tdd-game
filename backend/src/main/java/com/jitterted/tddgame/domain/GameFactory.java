package com.jitterted.tddgame.domain;

import java.util.List;

public class GameFactory {

  private final DeckFactory deckFactory;

  public GameFactory(DeckFactory deckFactory) {
    this.deckFactory = deckFactory;
  }

  public Game createTwoPlayerGame() {
    List<Player> playerList = List.of(new Player(), new Player());
    Deck deck = deckFactory.createStandardDeck();
    return new Game(playerList, deck);
  }
}
