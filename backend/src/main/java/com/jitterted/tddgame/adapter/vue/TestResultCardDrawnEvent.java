package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.DrawnTestResultCard;
import lombok.Data;

@Data
public class TestResultCardDrawnEvent {
  private final String action = "TestResultCardDrawn";
  private final String playerId;
  private final TestResultCardView testResultCardView;

  public TestResultCardDrawnEvent(DrawnTestResultCard drawnTestResultCard) {
    playerId = String.valueOf(drawnTestResultCard.player().id().getId());
    testResultCardView = TestResultCardView.of(drawnTestResultCard.card());
  }
}
