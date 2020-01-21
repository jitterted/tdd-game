package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayerId;
import lombok.Data;

@Data
public class DiscardedTestResultCardEvent {
  private final String action = "TestResultsCardDiscarded";
  private final String playerId;

  public DiscardedTestResultCardEvent(PlayerId playerId) {
    this.playerId = String.valueOf(playerId.getId());
  }
}
