package com.jitterted.tddgame.domain;

public enum OnPlayGoesTo {
  SELF {
    @Override
    public void play(Game game, Player player, PlayingCard playingCard) {
      player.inPlay().add(playingCard);
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
