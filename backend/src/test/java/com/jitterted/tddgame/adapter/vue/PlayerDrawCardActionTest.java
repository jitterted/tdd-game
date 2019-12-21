package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Hand;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerDrawCardActionTest {

  @Test
  public void newCardFromDeckPlacedIntoHand() throws Exception {
    PlayerAction drawAction = new PlayerAction(PlayerAction.DRAW_CARD);
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    Game game = gameService.currentGame();
    Player player0 = game.players().get(0);
    Hand hand = player0.hand();
    Card card = hand.cards().get(0);
    hand.remove(card.id());

    drawAction.executeFor(player0.id(), gameService);

    assertThat(hand.isFull())
      .isTrue();
  }
}
