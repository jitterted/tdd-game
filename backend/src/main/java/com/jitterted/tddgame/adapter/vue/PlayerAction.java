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
  public static final String DRAW_HAND = "DRAW_HAND";

  private String action;

  public void executeFor(PlayerId playerId, GameService gameService) {
    switch (action.toUpperCase()) {
      case DRAW_CARD:
        gameService.currentGame().drawCardFor(playerId);
        break;
      case DRAW_HAND:
        gameService.currentGame().drawToFullHandFor(playerId);
        break;
    }
  }
}
