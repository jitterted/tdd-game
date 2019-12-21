package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerGameViewTest {

  private Player player1;
  private Player player2;
  private Game game;

  @Test
  public void gameViewContainsInPlayCardsFromSpecifiedPlayer() throws Exception {
    setupGameWithTwoPlayersHavingCardsInPlayAndHand();

    PlayerGameView playerGameView = PlayerGameView.from(game, player1.id());

    assertThat(playerGameView.getInPlay().getCards())
      .extracting("title")
      .containsExactly("player 1 in Play");
  }

  @Test
  public void containsInHandCardsForSpecifiedPlayer() throws Exception {
    setupGameWithTwoPlayersHavingCardsInPlayAndHand();

    PlayerGameView playerGameView1 = PlayerGameView.from(game, player1.id());

    assertThat(playerGameView1.getHand().getCards())
      .extracting("title")
      .containsExactly("player 1 in Hand");

    PlayerGameView playerGameView2 = PlayerGameView.from(game, player2.id());

    assertThat(playerGameView2.getHand().getCards())
      .extracting("title")
      .containsExactly("player 2 in Hand");
  }

  @Test
  public void containsOpponentCardsForSpecifiedPlayer() throws Exception {
    setupGameWithTwoPlayersHavingCardsInPlayAndHand();

    PlayerGameView playerGameView1 = PlayerGameView.from(game, player1.id());

    assertThat(playerGameView1.getOpponentInPlay().getCards())
      .extracting("title")
      .containsExactly("player 2 in Play");

    PlayerGameView playerGameView2 = PlayerGameView.from(game, player2.id());

    assertThat(playerGameView2.getOpponentInPlay().getCards())
      .extracting("title")
      .containsExactly("player 1 in Play");
  }

  private void setupGameWithTwoPlayersHavingCardsInPlayAndHand() {
    player1 = new Player(PlayerId.of(1));
    player2 = new Player(PlayerId.of(2));
    CardFactory cardFactory = new CardFactory();
    Card cardPlayer1Play = cardFactory.card("player 1 in Play");
    Card cardPlayer2Play = cardFactory.card("player 2 in Play");
    Card cardPlayer1Hand = cardFactory.card("player 1 in Hand");
    Card cardPlayer2Hand = cardFactory.card("player 2 in Hand");
    player1.inPlay().add(cardPlayer1Play);
    player1.hand().add(cardPlayer1Hand);
    player2.inPlay().add(cardPlayer2Play);
    player2.hand().add(cardPlayer2Hand);
    game = new Game(List.of(player1, player2), null);
  }
}
