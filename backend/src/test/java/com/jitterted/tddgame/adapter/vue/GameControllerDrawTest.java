package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.CopyCardShuffler;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.DeckFactory;
import com.jitterted.tddgame.domain.DrawnTestResultCard;
import com.jitterted.tddgame.domain.FakeGameService;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameFactory;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Hand;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.PlayingCard;
import com.jitterted.tddgame.domain.PlayingCardDefinition;
import com.jitterted.tddgame.domain.TestResultCard;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import com.jitterted.tddgame.domain.port.GameStateChannel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameControllerDrawTest {

  @Test
  public void newGameThenPlayerHandIsFullWithFiveCards() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    Hand hand = player.hand();

    assertThat(hand.cards())
      .hasSize(5);
    assertThat(hand.isFull())
      .isTrue();
  }

  @Test
  public void drawCardActionResultsInNewCardDrawnToPlayerHand() throws Exception {
    DeckFactory stubDeckFactory = new StubWriteCodePlayingCardDeckFactory(new CardFactory());
    Game game = new GameFactory(stubDeckFactory, new PlayerFactory()).createTwoPlayerGame();
    game.start();
    FakeGameService gameService = new FakeGameService(game);
    GameController gameController = new GameController(gameService, mock(GameStateChannel.class));
    Player player = game.players().get(0);
    Hand hand = player.hand();
    CardId cardId = hand.cards().get(0).id();
    hand.remove(cardId); // make room in hand for draw from deck

    gameController.handleAction(String.valueOf(player.id().getId()),
                                new PlayerAction(PlayerAction.DRAW_CARD));

    assertThat(hand.cards())
      .hasSize(5);
    assertThat(player.hand().isFull())
      .isTrue();
  }

  @Test
  public void drawHandActionResultsInHandFull() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameStateChannel dummy = mock(GameStateChannel.class);
    GameController gameController = new GameController(gameService, dummy);
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    Hand hand = player.hand(); // initial hand is 5 cards
    hand.remove(hand.cards().get(0).id());
    hand.remove(hand.cards().get(0).id());

    gameController.handleAction(String.valueOf(player.id().getId()),
                                new PlayerAction(PlayerAction.DRAW_HAND));

    assertThat(player.hand().isFull())
      .isTrue();
  }

  @Test
  public void testResultCardDrawPutsCardInGameState() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new Deck<>(new CopyCardShuffler<>());
    TestResultCard testResultCard = new TestResultCard(CardId.of(1759), "As Predicted");
    testResultCardDeck.addToDrawPile(testResultCard);

    Player player1 = new Player(PlayerId.of(0));
    Game game = new Game(List.of(player1), null, testResultCardDeck);
    GameService gameService = new FakeGameService(game);

    GameStateChannelSpy gameStateChannelSpy = new GameStateChannelSpy();
    GameController gameController = new GameController(gameService, gameStateChannelSpy);

    gameController.handleDrawTestResultCard(String.valueOf(player1.id().getId()));

    assertThat(gameStateChannelSpy.getDrawnCard())
      .isEqualTo(new DrawnTestResultCard(testResultCard, player1));
  }

  @Test
  public void testResultCardDrawSendsEventThroughMessageChannel() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new Deck<>(new CopyCardShuffler<>());
    TestResultCard testResultCard = new TestResultCard(CardId.of(1759), "As Predicted");
    testResultCardDeck.addToDrawPile(testResultCard);

    Player player1 = new Player(PlayerId.of(0));
    Game game = new Game(List.of(player1), null, testResultCardDeck);
    GameService gameService = new FakeGameService(game);

    SimpMessagingTemplate spySimpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    GameStateChannel gameStateChannel = new StompGameStateChannel(spySimpMessagingTemplate, new SyncTaskExecutor());

    GameController gameController = new GameController(gameService, gameStateChannel);

    gameController.handleDrawTestResultCard(String.valueOf(player1.id().getId()));

    TestResultCardDrawnEvent expectedEvent = new TestResultCardDrawnEvent(
      new DrawnTestResultCard(testResultCard, player1));
    verify(spySimpMessagingTemplate).convertAndSend("/topic/testresultcard", expectedEvent);
  }

  @Test
  public void discardTestResultsCardDiscardsToDiscardPileAndRemovedFromGameState() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new Deck<>(new CopyCardShuffler<>());
    TestResultCard testResultCard = new TestResultCard(CardId.of(1759), "As Predicted");
    testResultCardDeck.addToDrawPile(testResultCard);

    Player player1 = new Player(PlayerId.of(0));
    Game game = new Game(List.of(player1), null, testResultCardDeck);
    game.drawTestResultCardFor(player1.id());
    GameService gameService = new FakeGameService(game);

    SimpMessagingTemplate spySimpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    GameStateChannel gameStateChannel = new StompGameStateChannel(spySimpMessagingTemplate, new SyncTaskExecutor());
    GameController gameController = new GameController(gameService, gameStateChannel);

    gameController.handleDiscardTestResultCard(String.valueOf(player1.id().getId()));

    TestResultCardDiscardedEvent expectedEvent = new TestResultCardDiscardedEvent(player1.id());
    verify(spySimpMessagingTemplate).convertAndSend("/topic/testresultcard", expectedEvent);
  }

  private static class StubWriteCodePlayingCardDeckFactory implements DeckFactory {
    private final CardFactory cardFactory;

    public StubWriteCodePlayingCardDeckFactory(CardFactory cardFactory) {
      this.cardFactory = cardFactory;
    }

    @Override
    public Deck<PlayingCard> createPlayingCardDeck() {
      Deck<PlayingCard> deck = new Deck<>(new CopyCardShuffler<>());
      for (int i = 0; i < 20; i++) {
        deck.addToDiscardPile(cardFactory.playingCard(PlayingCardDefinition.WRITE_CODE));
      }
      return deck;
    }

    @Override
    public Deck<TestResultCard> createTestResultCardDeck() {
      return new Deck<>(new CopyCardShuffler<>());
    }
  }
}
