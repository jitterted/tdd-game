package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.assumeThat;

class PlayerPlayTest {

  @Test
  public void playerPlaysCardThenCardIsInPlay() throws Exception {
    Player player = new Player(PlayerId.of(0));
    PlayingCard playingCard = new CardFactory().playingCard("to play", Usage.SELF);
    player.hand().add(playingCard);

    player.play(null, playingCard.id());

    assertThat(player.hand().contains(playingCard))
      .isFalse();
    assertThat(player.inPlay().isEmpty())
      .isFalse();
    assertThat(player.inPlay().contains(playingCard))
      .isTrue();
  }

  @Test
  public void playerPlaysAttackCardThenCardIsInOpponentInPlay() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Player opponent = new Player(PlayerId.of(1));
    Game game = new Game(List.of(player, opponent), null, null);

    PlayingCard playingCard = new CardFactory().playingCard("attack", Usage.OPPONENT);
    player.hand().add(playingCard);

    player.play(game, playingCard.id());

    assertThat(opponent.inPlay().cards())
      .containsOnly(playingCard);
    assertThat(player.inPlay().isEmpty())
      .isTrue();
  }

  @Test
  public void playedRefactorCardIsDiscarded() throws Exception {
    Player player = new Player(PlayerId.of(0));
    PlayingCard playingCard = new CardFactory().playingCard("design refactor", Usage.DISCARD);
    player.hand().add(playingCard);
    Deck<PlayingCard> deck = new Deck<>(null);
    Game game = new Game(List.of(player), deck, null);

    assumeThat(player.hand().cards())
      .contains(playingCard);

    player.play(game, playingCard.id());

    assertThat(game.deck().discardPile())
      .containsOnly(playingCard);
  }
}
