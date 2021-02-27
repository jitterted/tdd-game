package com.jitterted.tddgame.domain;

import com.jitterted.tddgame.domain.port.GameStateChannel;

public class TwoPlayerGameService implements GameService {
  private Game game;

  public TwoPlayerGameService(PlayerFactory playerFactory) {
    DeckFactory deckFactory = new DefaultDeckFactory(new CardFactory());
    game = new GameFactory(deckFactory, playerFactory).createTwoPlayerGame();
    game.start();
  }

  public TwoPlayerGameService(PlayerFactory playerFactory, GameStateChannel gameStateChannel) {
    DeckFactory deckFactory = new DefaultDeckFactory(new CardFactory());
    GameFactory gameFactory = new GameFactory(deckFactory, playerFactory, gameStateChannel);
    game = gameFactory.createTwoPlayerGame();
    game.start();
  }

  @Override
  public Game currentGame() {
    return game;
  }

  @Override
  public Player assignNextAvailablePlayerToUser(User user) {
    Player unassignedPlayer = game.players()
                                  .stream()
                                  .filter(player -> !player.isAssigned())
                                  .findAny()
                                  .orElseThrow(() -> new IllegalStateException("No unassigned players"));
    unassignedPlayer.assignUser(user);
    return unassignedPlayer;
  }
}
