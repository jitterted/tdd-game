package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayerId;
import lombok.Data;

@Data
public class TestResultCardDiscardedEvent {
  private final String action = "TestResultCardDiscarded";
  private final String playerId;

  public TestResultCardDiscardedEvent(PlayerId playerId) {
    this.playerId = String.valueOf(playerId.getId());
  }
}
