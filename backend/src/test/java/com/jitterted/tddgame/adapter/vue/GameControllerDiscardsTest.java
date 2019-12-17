package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerDiscardsTest {

  @Test
  public void discardFromHandIsTransferredToDeckDiscardPile() throws Exception {
    GameService gameService = new TwoPlayerGameService();
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Deck deck = game.deck();
    Player player = game.players().get(0);
    Card cardFromHand = player.hand().cards().get(0);

    gameController.discard(String.valueOf(player.id().getId()),
                           new DiscardAction(cardFromHand.id().getId(), DiscardAction.SOURCE_HAND));

    assertThat(deck.discardPile())
      .containsExactly(cardFromHand);
  }

  @Test
  public void discardFromInPlayIsTransferredToDeckDiscardPile() throws Exception {
    GameService gameService = new TwoPlayerGameService();
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Deck deck = game.deck();
    Player player = game.players().get(0);
    Card cardFromInPlay = player.hand().cards().get(0);
    game.playCardFor(player.id(), cardFromInPlay.id());

    gameController.discard(String.valueOf(player.id().getId()),
                           new DiscardAction(cardFromInPlay.id().getId(), DiscardAction.SOURCE_IN_PLAY));

    assertThat(deck.discardPile())
      .containsExactly(cardFromInPlay);
  }

}
