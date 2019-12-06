package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Player;
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

  @Test
  public void playerDiscardIsTransferredToDeckDiscardPile() throws Exception {
    GameService gameService = new TwoPlayerGameService();
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Deck deck = game.deck();
    Player player = game.players().get(0);
    Card cardFromHand = player.hand().cards().get(0);

    gameController.discardFromHand(String.valueOf(player.id().getId()),
                                   new CardIdDto(cardFromHand.id().getId()));

    assertThat(deck.discardPile())
      .contains(cardFromHand);
  }

}
