package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.assumeThat;

class PlayerPlayTest {

  @Test
  public void playerPlaysCardThenCardIsInPlay() throws Exception {
    Player player = new Player(PlayerId.of(0));
    PlayingCard playingCard = new CardFactory().playingCard("to play", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF);
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
  public void playedRefactorCardIsDiscarded() throws Exception {
    Player player = new Player(PlayerId.of(0));
    PlayingCard playingCard = new CardFactory().playingCard("refactor", OnDrawGoesTo.HAND, OnPlayGoesTo.DISCARD);
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
