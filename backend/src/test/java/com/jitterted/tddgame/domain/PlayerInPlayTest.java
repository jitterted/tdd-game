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
    Card card = new CardFactory().card("to play");
    player.hand().add(card);

    player.play(card.id());

    assertThat(player.hand().contains(card))
      .isFalse();
    assertThat(player.inPlay().isEmpty())
      .isFalse();
    assertThat(player.inPlay().contains(card))
      .isTrue();
  }

}
