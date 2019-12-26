package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

class PlayerPlayTest {

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
    Player player = new Player(PlayerId.of(0));
    Player opponent = new Player(PlayerId.of(1));
    Game game = new Game(List.of(player, opponent), null);

    Card card = new CardFactory().card("attack", Usage.OPPONENT);
    player.hand().add(card);

    player.play(game, card.id());

    assertThat(opponent.inPlay().cards())
      .containsOnly(card);
    assertThat(player.inPlay().isEmpty())
      .isTrue();
  }

  @Test
  public void playedRefactorCardIsDiscarded() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Card card = new CardFactory().card("refactor", Usage.DISCARD);
    player.hand().add(card);
    Deck deck = new Deck(null);
    Game game = new Game(List.of(player), deck);

    assumeThat(player.hand().cards())
      .contains(card);

    player.play(game, card.id());

    assertThat(game.deck().discardPile())
      .containsOnly(card);
  }
}
