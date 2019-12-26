package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

  @Test
  public void newGameWhenStartsIsInInitialGameState() throws Exception {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();
    game.start();

    assertThat(game.players())
      .hasSize(2);
    assertThat(game.players().get(0).hand().count())
      .isEqualTo(5);
    assertThat(game.players().get(1).hand().count())
      .isEqualTo(5);
    assertThat(game.deck())
      .isNotNull();
  }

  @Test
  public void opponentForPlayerIsOtherPlayer() throws Exception {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();

    Player player1 = game.players().get(0);
    Player player2 = game.players().get(1);

    assertThat(game.opponentFor(player1))
      .isEqualTo(player2);
    assertThat(game.opponentFor(player2))
      .isEqualTo(player1);
  }

  @Test
  public void playedAttackCardIsMovedToOpponentInPlay() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Player opponent = new Player(PlayerId.of(1));
    Game game = new Game(List.of(player, opponent), null);
    Card attack = new CardFactory().card("attack", Usage.OPPONENT);
    player.hand().add(attack);

    game.playCardFor(player.id(), attack.id());

    assertThat(player.inPlay().cards())
      .isEmpty();
    assertThat(opponent.inPlay().cards())
      .containsOnly(attack);
  }

  @Test
  public void playerDiscardOfCardIsTransferredToDeckDiscardPile() throws Exception {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();
    game.start();

    Player player = game.players().get(0);
    Card cardFromHand = player.hand().cards().get(0);

    game.discardFromHand(player.id(), cardFromHand.id());

    assertThat(game.deck().discardPile())
      .containsExactly(cardFromHand);
    assertThat(player.hand().contains(cardFromHand))
      .isFalse();
  }

  @Test
  public void playerWithRoomInHandDrawsCardThenCardFromDrawPileInHand() throws Exception {
    Deck deck = new Deck(null);
    Card card = new Card(CardId.of(1), "card1", null);
    deck.addToDrawPile(card);
    List<Player> twoPlayers = new PlayerFactory().createTwoPlayers();
    Player player0 = twoPlayers.get(0);
    Game game = new Game(twoPlayers, deck);

    game.drawCardFor(player0.id());

    assertThat(deck.drawPileSize())
      .isZero();
    assertThat(player0.hand().cards())
      .containsExactly(card);
  }
}
