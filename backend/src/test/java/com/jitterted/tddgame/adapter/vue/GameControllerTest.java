package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.GameService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameControllerTest {

  @Test
  public void newGameIsNoCardsDealt() throws Exception {
    GameService gameService = new TwoPlayerGameService();
    GameController gameController = new GameController(gameService);

    PlayerGameView playerGameView = gameController.playerGameView();

    assertThat(playerGameView.getHand().getCards())
      .hasSize(5);
  }

}
