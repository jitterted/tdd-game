package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Hand;
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

    gameController.discardFromHand(playerIdStringFrom(player),
                                   new CardIdDto(cardFromHand.id().getId()));

    assertThat(deck.discardPile())
      .contains(cardFromHand);
  }

  @Test
  public void playedCardIsTransferredFromHandToInPlay() throws Exception {
    GameService gameService = new TwoPlayerGameService();
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    Card cardFromHand = player.hand().cards().get(0);

    gameController.playCard(playerIdStringFrom(player),
                            new CardIdDto(cardFromHand.id().getId()));

    assertThat(player.inPlay().cards())
      .contains(cardFromHand);

  }


  @Test
  public void playerDrawActionResultsInNewCardDrawnToPlayerHand() throws Exception {
    GameService gameService = new TwoPlayerGameService();
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    Hand hand = player.hand();
    CardId cardId = hand.cards().get(0).id();
    hand.remove(cardId); // make room in hand for draw from deck

    gameController.handleAction(playerIdStringFrom(player), new PlayerAction(PlayerAction.DRAW_CARD));

    assertThat(player.hand().isFull())
      .isTrue();
  }

  private String playerIdStringFrom(Player player) {
    return String.valueOf(player.id().getId());
  }

}
