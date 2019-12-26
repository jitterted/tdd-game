package com.jitterted.tddgame.domain;

public enum Usage {
  SELF{
    @Override
    public void play(Card card, Player player, Player opponent) {
      player.inPlay().add(card);
    }
  },
  OPPONENT{
    @Override
    public void play(Card card, Player player, Player opponent) {
      opponent.inPlay().add(card);
    }
  },
  ;

  public abstract void play(Card card, Player player, Player opponent);
}
