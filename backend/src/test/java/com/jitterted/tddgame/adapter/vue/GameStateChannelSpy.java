package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.DrawnTestResultCard;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.port.GameStateChannel;

public class GameStateChannelSpy implements GameStateChannel {
  private DrawnTestResultCard drawnCard;

  public DrawnTestResultCard getDrawnCard() {
    return drawnCard;
  }

  @Override
  public void testResultCardDrawn(DrawnTestResultCard drawnTestResultCard) {
    drawnCard = drawnTestResultCard;
  }

  @Override
  public void testResultCardDiscarded(PlayerId playerId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void playerActed(Game game) {
    throw new UnsupportedOperationException();
  }
}
