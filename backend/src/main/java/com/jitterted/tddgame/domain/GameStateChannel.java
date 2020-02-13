package com.jitterted.tddgame.domain;

public interface GameStateChannel {
  void testResultCardDrawn(DrawnTestResultCard drawnTestResultCard);

  void testResultCardDiscarded(PlayerId playerId);

  void cardPlayed(Game game);
}
