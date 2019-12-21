package com.jitterted.tddgame.domain;

public interface GameService {
  Game currentGame();

  Player assignNextAvailablePlayerToUser(User user);
}
