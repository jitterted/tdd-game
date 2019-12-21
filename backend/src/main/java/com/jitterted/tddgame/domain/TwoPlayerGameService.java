package com.jitterted.tddgame.domain;

public class TwoPlayerGameService implements GameService {
  private Game game;

  public TwoPlayerGameService(PlayerFactory playerFactory) {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    game = new GameFactory(deckFactory, playerFactory).createTwoPlayerGame();
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
