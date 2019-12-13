package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import lombok.Data;

@Data
public class PlayerGameView {
  private final CardsView hand;
  private final CardsView inPlay;
  private final CardsView opponentInPlay = CardsView.of(new Card(CardId.of(98), "predict"));

  public static PlayerGameView from(Game game) {
    Player player = game.players().get(0);
    CardsView handView = CardsView.from(player.hand().cards());
    CardsView inPlayView = CardsView.from(player.inPlay().cards());
    return new PlayerGameView(handView, inPlayView);
  }
}
