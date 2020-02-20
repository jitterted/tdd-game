package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.CopyCardShuffler;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.DrawnTestResultCard;
import com.jitterted.tddgame.domain.FakeGameService;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.GameStateChannel;
import com.jitterted.tddgame.domain.Hand;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.TestResultCard;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameControllerDrawTest {

  @Test
  public void drawCardActionResultsInNewCardDrawnToPlayerHand() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService, mock(GameStateChannel.class));
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    Hand hand = player.hand();
    CardId cardId = hand.cards().get(0).id();
    hand.remove(cardId); // make room in hand for draw from deck

    gameController.handleAction(String.valueOf(player.id().getId()),
                                new PlayerAction(PlayerAction.DRAW_CARD));

    assertThat(player.hand().isFull())
      .isTrue();
  }

  @Test
  public void drawHandActionResultsInHandFull() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService, mock(GameStateChannel.class));
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
    GameStateChannel gameStateChannel = new StompGameStateChannel(spySimpMessagingTemplate);

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
    GameStateChannel gameStateChannel = new StompGameStateChannel(spySimpMessagingTemplate);
    GameController gameController = new GameController(gameService, gameStateChannel);

    gameController.handleDiscardTestResultCard(String.valueOf(player1.id().getId()));

    TestResultCardDiscardedEvent expectedEvent = new TestResultCardDiscardedEvent(player1.id());
    verify(spySimpMessagingTemplate).convertAndSend("/topic/testresultcard", expectedEvent);
  }
}
