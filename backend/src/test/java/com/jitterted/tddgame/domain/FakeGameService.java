package com.jitterted.tddgame.domain;

public class FakeGameService implements GameService {
  private final Game game;

  public FakeGameService(Game game) {
    this.game = game;
  }

  @Override
  public Game currentGame() {
    return game;
  }

  @Override
  public Player assignNextAvailablePlayerToUser(User user) {
    throw new UnsupportedOperationException();
  }
}
