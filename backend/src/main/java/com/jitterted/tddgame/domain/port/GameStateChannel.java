package com.jitterted.tddgame.domain.port;

import com.jitterted.tddgame.domain.DrawnTestResultCard;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.PlayerId;

// PORT for the Broadcast adapter
public interface GameStateChannel {
  void testResultCardDrawn(DrawnTestResultCard drawnTestResultCard);

  void testResultCardDiscarded(PlayerId playerId);

  void playerActed(Game game);
}
