package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.DrawnTestResultCard;
import lombok.Data;

@Data
public class DrawnTestResultCardEvent {
  private final String action = "TestResultsCardDrawn";
  private final String playerId;
  private final TestResultCardView testResultCardView;

  public DrawnTestResultCardEvent(DrawnTestResultCard drawnTestResultCard) {
    playerId = String.valueOf(drawnTestResultCard.player().id().getId());
    testResultCardView = TestResultCardView.of(drawnTestResultCard.card());
  }
}
