package com.jitterted.tddgame.domain;

public enum Usage {
  SELF{
    @Override
    public void play(Game game, Player player, Card card) {
      player.inPlay().add(card);
    }
  },
  OPPONENT{
    @Override
    public void play(Game game, Player player, Card card) {
      game.opponentFor(player).inPlay().add(card);
    }
  },
  DISCARD {
    @Override
    public void play(Game game, Player player, Card card) {
      game.deck().addToDiscardPile(card);
    }
  };

  public abstract void play(Game game, Player player, Card card);
}
