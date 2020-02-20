package com.jitterted.tddgame.domain;

public enum Usage {
  SELF {
    @Override
    public void play(Game game, Player player, PlayingCard playingCard) {
      player.inPlay().add(playingCard);
    }
  },
  OPPONENT {
    @Override
    public void play(Game game, Player player, PlayingCard playingCard) {
      game.opponentFor(player).inPlay().add(playingCard);
    }
  },
  DISCARD {
    @Override
    public void play(Game game, Player player, PlayingCard playingCard) {
      game.deck().addToDiscardPile(playingCard);
    }
  };

  public abstract void play(Game game, Player player, PlayingCard playingCard);
}
