package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerGameViewTest {

  @Test
  public void gameViewContainsInPlayCardsFromPlayer() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Card card = new Card(CardId.of(0), "zero");
    player.inPlay().add(card);
    Game game = new Game(List.of(player), null);

    PlayerGameView playerGameView = PlayerGameView.from(game);

    assertThat(playerGameView.getInPlay().getCards())
      .containsExactly(new CardDto("zero", 0));
  }
}
