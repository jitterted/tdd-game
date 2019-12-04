package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Game;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PlayerGameView {
  private final HandDto hand;
  private final List<CardDto> inPlay = Collections.emptyList();
  private final List<CardDto> opponentInPlay = Collections.emptyList();

  public static PlayerGameView from(Game game) {
    HandDto handDto = HandDto.from(game.players().get(0).hand());
    return new PlayerGameView(handDto);
  }
}
