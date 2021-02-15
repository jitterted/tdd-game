package com.jitterted.tddgame.domain;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

  @Test
  public void newGameWhenStartsIsInInitialGameState() throws Exception {
    DeckFactory deckFactory = new DefaultDeckFactory(new CardFactory());
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
    DeckFactory deckFactory = new DefaultDeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();

    Player player1 = game.players().get(0);
    Player player2 = game.players().get(1);

    assertThat(game.opponentFor(player1))
      .isEqualTo(player2);
    assertThat(game.opponentFor(player2))
      .isEqualTo(player1);
  }

  @Test
  public void playerDiscardOfCardIsTransferredToDeckDiscardPile() throws Exception {
    DeckFactory deckFactory = new DefaultDeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();
    game.start();

    Player player = game.players().get(0);
    PlayingCard playingCardFromHand = player.hand().cards().get(0);

    game.discardFromHand(player.id(), playingCardFromHand.id());

    assertThat(game.deck().discardPile())
      .containsExactly(playingCardFromHand);
    assertThat(player.hand().contains(playingCardFromHand))
      .isFalse();
  }

  @Test
  public void playerWithNotFullHandDrawsCardThenCardFromDrawPileInHand() throws Exception {
    Deck<PlayingCard> deck = new Deck<>(null);
    PlayingCard playingCard = new CardFactory().playingCard("card1", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF);
    deck.addToDrawPile(playingCard);
    List<Player> twoPlayers = new PlayerFactory().createTwoPlayers();
    Player player0 = twoPlayers.get(0);
    Game game = new Game(twoPlayers, deck, null);

    game.drawCardFor(player0.id());

    assertThat(deck.drawPileSize())
      .isZero();
    assertThat(player0.hand().cards())
      .containsExactly(playingCard);
  }

  @Test
  public void playerWithTwoCardsInHandDrawsToFullHandThenHandIsFull() throws Exception {
    Deck<PlayingCard> deck = createPlayingCardDeckDrawPileFilledWith(OnPlayGoesTo.SELF, 10);
    List<Player> twoPlayers = new PlayerFactory().createTwoPlayers();
    Player player0 = twoPlayers.get(0);
    player0.drawFrom(deck);
    player0.drawFrom(deck);
    Game game = new Game(twoPlayers, deck, null);

    game.drawToFullHandFor(player0.id());

    assertThat(player0.hand().isFull())
      .isTrue();
  }

  @NotNull
  private Deck<PlayingCard> createPlayingCardDeckDrawPileFilledWith(OnPlayGoesTo onPlayGoesTo, int cardCount) {
    Deck<PlayingCard> deck = new Deck<>(new DummyCardShuffler<>());
    CardFactory cardFactory = new CardFactory();
    for (int i = 0; i < cardCount; i++) {
      deck.addToDrawPile(cardFactory.playingCard("card", OnDrawGoesTo.HAND, onPlayGoesTo));
    }
    return deck;
  }


  @Test
  public void drawingCardFromTestResultDeckPutsCardInDrawnTestCardInGameState() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new Deck<>(new DummyCardShuffler<>());
    TestResultCard testResultCard = new TestResultCard(CardId.of(3), "As Predicted");
    testResultCardDeck.addToDrawPile(testResultCard);
    List<Player> twoPlayers = new PlayerFactory().createTwoPlayers();
    Game game = new Game(twoPlayers, null, testResultCardDeck);
    Player player1 = twoPlayers.get(0);

    game.drawTestResultCardFor(player1.id());

    assertThat(game.drawnTestResultCard())
      .isEqualTo(new DrawnTestResultCard(testResultCard, player1));
    assertThat(testResultCardDeck.drawPileSize())
      .isZero();
    assertThat(testResultCardDeck.discardPile())
      .isEmpty();
  }

  @Test
  public void drawingTestResultCardWhenOneAlreadyDrawnThrowsException() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new DefaultDeckFactory(new CardFactory()).createTestResultCardDeck();
    List<Player> twoPlayers = new PlayerFactory().createTwoPlayers();
    Game game = new Game(twoPlayers, null, testResultCardDeck);
    Player player1 = twoPlayers.get(0);
    game.drawTestResultCardFor(player1.id());

    assertThatThrownBy(() -> {
      game.drawTestResultCardFor(player1.id());
    }).isInstanceOf(TestResultCardAlreadyDrawnException.class);
  }

  @Test
  public void discardTestResultCardMovesToDiscardPileAndClearsGameState() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new DefaultDeckFactory(new CardFactory()).createTestResultCardDeck();
    List<Player> twoPlayers = new PlayerFactory().createTwoPlayers();
    Game game = new Game(twoPlayers, null, testResultCardDeck);
    Player player1 = twoPlayers.get(0);
    game.drawTestResultCardFor(player1.id());
    TestResultCard drawnTestResultCard = game.drawnTestResultCard().card();

    game.discardTestResultCardFor(player1.id());

    assertThat(testResultCardDeck.discardPile())
      .containsOnly(drawnTestResultCard);
    assertThat(game.drawnTestResultCard())
      .isNull();
  }

  @Test
  public void discardTestResultCardByWrongPlayerThrowsException() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new DefaultDeckFactory(new CardFactory()).createTestResultCardDeck();
    List<Player> twoPlayers = new PlayerFactory().createTwoPlayers();
    Game game = new Game(twoPlayers, null, testResultCardDeck);
    Player player1 = twoPlayers.get(0);
    Player player2 = twoPlayers.get(1);
    game.drawTestResultCardFor(player1.id());

    assertThatThrownBy(() -> {
      game.discardTestResultCardFor(player2.id());
    }).isInstanceOf(WrongPlayerDiscardingTestResultCardException.class);
  }
}
