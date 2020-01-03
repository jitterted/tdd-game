package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InPlayTest {

  @Test
  public void newPlayerInPlayIsEmpty() throws Exception {
    Player player = new Player(PlayerId.of(0));

    assertThat(player.inPlay().isEmpty())
      .isTrue();
  }

  @Test
  public void inPlayAddTwoCardsAreReturned() throws Exception {
    CardFactory cardFactory = new CardFactory();
    PlayingCard playingCard1 = cardFactory.playingCard("one", Usage.SELF);
    PlayingCard playingCard2 = cardFactory.playingCard("two", Usage.SELF);

    InPlay inPlay = new InPlay();

    inPlay.add(playingCard1);
    inPlay.add(playingCard2);

    assertThat(inPlay.cards())
      .containsExactly(playingCard1, playingCard2);
  }

  @Test
  public void discardFromInPlayThatIsNotInPlayIsAnException() throws Exception {
    CardFactory cardFactory = new CardFactory();
    PlayingCard playingCard1 = cardFactory.playingCard("one", Usage.SELF);
    PlayingCard playingCard2 = cardFactory.playingCard("two", Usage.SELF);
    InPlay inPlay = new InPlay();
    inPlay.add(playingCard1);
    inPlay.add(playingCard2);

    PlayingCard notInPlay = cardFactory.playingCard("not in play", Usage.SELF);

    assertThatThrownBy(() -> {inPlay.remove(notInPlay.id());})
      .isInstanceOf(CardNotInPlayException.class);
  }
}
