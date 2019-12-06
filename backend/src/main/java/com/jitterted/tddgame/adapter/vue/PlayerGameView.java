package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.Game;
import lombok.Data;

@Data
public class PlayerGameView {
  private final CardsView hand;
  private final CardsView inPlay = CardsView.of(new Card(CardId.of(79), "predict"), new Card(CardId.of(87), "predict"));
  private final CardsView opponentInPlay = CardsView.of(new Card(CardId.of(98), "predict"));

  public static PlayerGameView from(Game game) {
    CardsView cardsView = CardsView.from(game.players().get(0).hand());
    return new PlayerGameView(cardsView);
  }
}
