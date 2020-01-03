package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayingCard;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GameControllerDiscardsTest {

  @Test
  public void discardFromHandIsTransferredToDeckDiscardPile() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Deck deck = game.deck();
    Player player = game.players().get(0);
    PlayingCard playingCardFromHand = player.hand().cards().get(0);

    gameController.discard(String.valueOf(player.id().getId()),
                           new DiscardAction(playingCardFromHand.id().getId(), DiscardAction.SOURCE_HAND));

    assertThat(deck.discardPile())
      .containsExactly(playingCardFromHand);
  }

  @Test
  public void discardFromInPlayIsTransferredToDeckDiscardPile() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Deck deck = game.deck();
    Player player = game.players().get(0);
    PlayingCard playingCardFromInPlay = player.hand().cards().get(0);
    game.playCardFor(player.id(), playingCardFromInPlay.id());

    gameController.discard(String.valueOf(player.id().getId()),
                           new DiscardAction(playingCardFromInPlay.id().getId(), DiscardAction.SOURCE_IN_PLAY));

    assertThat(deck.discardPile())
      .containsExactly(playingCardFromInPlay);
  }

}
