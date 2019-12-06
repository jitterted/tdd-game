package com.jitterted.tddgame.domain;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerFactory {
  private final AtomicInteger idSequence = new AtomicInteger(0);

  private Player createPlayer() {
    return new Player(PlayerId.of(idSequence.getAndIncrement()));
  }

  public List<Player> createTwoPlayers() {
    return List.of(createPlayer(), createPlayer());
  }
}
