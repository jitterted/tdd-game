package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.OnDrawGoesTo;
import com.jitterted.tddgame.domain.OnPlayGoesTo;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.PlayingCard;
import com.jitterted.tddgame.domain.User;
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

  @Test
  public void playersWithAssignedUsersAreReturned() throws Exception {
    player1 = new Player(PlayerId.of(1));
    player1.assignUser(new User("first"));
    player2 = new Player(PlayerId.of(2));
    player2.assignUser(new User("opponent"));
    Game game = new Game(List.of(player1, player2), null, null);

    PlayerGameView playerGameView1 = PlayerGameView.from(game, player1.id());
    assertThat(playerGameView1.getPlayer().getName())
      .isEqualTo("first");
    assertThat(playerGameView1.getOpponent().getName())
      .isEqualTo("opponent");

    PlayerGameView playerGameView2 = PlayerGameView.from(game, player2.id());
    assertThat(playerGameView2.getPlayer().getName())
      .isEqualTo("opponent");
    assertThat(playerGameView2.getOpponent().getName())
      .isEqualTo("first");
  }

  @Test
  public void noPlayersHaveAssignedUsersReturnsNobodyForAllPlayers() throws Exception {
    player1 = new Player(PlayerId.of(1));
    player2 = new Player(PlayerId.of(2));
    Game game = new Game(List.of(player1, player2), null, null);

    PlayerGameView playerGameView1 = PlayerGameView.from(game, player1.id());
    assertThat(playerGameView1.getPlayer().getName())
      .isEqualTo("nobody");

    PlayerGameView playerGameView2 = PlayerGameView.from(game, player2.id());
    assertThat(playerGameView2.getPlayer().getName())
      .isEqualTo("nobody");
  }

  @Test
  public void onlyPlayer1AssignedReturnsNobodyForPlayer2() throws Exception {
    player1 = new Player(PlayerId.of(1));
    player1.assignUser(new User("first"));
    player2 = new Player(PlayerId.of(2));
    Game game = new Game(List.of(player1, player2), null, null);

    PlayerGameView playerGameView1 = PlayerGameView.from(game, player1.id());
    assertThat(playerGameView1.getOpponent().getName())
      .isEqualTo("nobody");
  }

  private void setupGameWithTwoPlayersHavingCardsInPlayAndHand() {
    player1 = new Player(PlayerId.of(1));
    player2 = new Player(PlayerId.of(2));
    CardFactory cardFactory = new CardFactory();
    PlayingCard playingCardPlayer1Play = cardFactory.playingCard("player 1 in Play", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    PlayingCard playingCardPlayer2Play = cardFactory.playingCard("player 2 in Play", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    PlayingCard playingCardPlayer1Hand = cardFactory.playingCard("player 1 in Hand", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    PlayingCard playingCardPlayer2Hand = cardFactory.playingCard("player 2 in Hand", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    player1.inPlay().add(playingCardPlayer1Play);
    player1.hand().add(playingCardPlayer1Hand);
    player2.inPlay().add(playingCardPlayer2Play);
    player2.hand().add(playingCardPlayer2Hand);
    game = new Game(List.of(player1, player2), null, null);
  }
}
