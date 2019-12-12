package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAction {
  public static final String DRAW_CARD = "DRAW_CARD";
  private String action;

  public void executeFor(PlayerId playerId, GameService gameService) {
    gameService.currentGame().drawCardFor(playerId);
  }
}
