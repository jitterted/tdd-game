package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerInPlayTest {

  @Test
  public void newPlayerInPlayIsEmpty() throws Exception {
    Player player = new Player(PlayerId.of(0));

    assertThat(player.inPlay().isEmpty())
      .isTrue();
  }

  @Test
  public void playerPlaysCardThenCardIsInPlay() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Card card = new CardFactory().card("to play", Usage.SELF);
    player.hand().add(card);

    player.play(null, card.id());

    assertThat(player.hand().contains(card))
      .isFalse();
    assertThat(player.inPlay().isEmpty())
      .isFalse();
    assertThat(player.inPlay().contains(card))
      .isTrue();
  }

  @Test
  public void playerPlaysAttackCardThenCardIsInOpponentInPlay() throws Exception {
    Player player1 = new Player(PlayerId.of(0));
    Player player2 = new Player(PlayerId.of(1));

    Card card = new CardFactory().card("attack", Usage.OPPONENT);
    player1.hand().add(card);

    player1.play(player2, card.id());

    assertThat(player2.inPlay().cards())
      .containsOnly(card);
    assertThat(player1.inPlay().isEmpty())
      .isTrue();
  }
}
