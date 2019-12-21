package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerId;
import lombok.Data;

@Data
public class PlayerGameView {
  private final CardsView hand;
  private final CardsView inPlay;
  private final CardsView opponentInPlay;

  public static PlayerGameView from(Game game, PlayerId playerId) {
    Player player = game.playerFor(playerId);
    CardsView handView = CardsView.from(player.hand().cards());
    CardsView inPlayView = CardsView.from(player.inPlay().cards());

    Player opponent = game.opponentFor(player);
    CardsView opponentInPlayView = CardsView.from(opponent.inPlay().cards());

    return new PlayerGameView(handView, inPlayView, opponentInPlayView);
  }
}
