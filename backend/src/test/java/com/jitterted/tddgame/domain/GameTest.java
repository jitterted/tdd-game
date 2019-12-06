package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

  @Test
  public void newGameWhenStartsIsInInitialGameState() throws Exception {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();
    game.start();

    assertThat(game.players().size())
      .isEqualTo(2);
    assertThat(game.players().get(0).hand().count())
      .isEqualTo(5);
    assertThat(game.players().get(1).hand().count())
      .isEqualTo(5);
    assertThat(game.deck())
      .isNotNull();
  }

  @Test
  public void playerDiscardOfCardIsTransferredToDeckDiscardPile() throws Exception {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();
    game.start();

    Player player = game.players().get(0);
    Card cardFromHand = player.hand().cards().get(0);

    game.discard(player.id(), cardFromHand.id());

    assertThat(game.deck().discardPile())
      .containsExactly(cardFromHand);
    assertThat(player.hand().contains(cardFromHand))
      .isFalse();
  }

}
