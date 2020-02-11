package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.CopyCardShuffler;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.FakeGameService;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Hand;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.TestResultCard;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class GameControllerDrawTest {

  @Test
  public void playerDrawActionResultsInNewCardDrawnToPlayerHand() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService, null);
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
  public void testResultCardDrawPutsCardInGameState() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new Deck<>(new CopyCardShuffler<>());
    TestResultCard testResultCard = new TestResultCard(CardId.of(1759), "As Predicted");
    testResultCardDeck.addToDrawPile(testResultCard);

    Player player1 = new Player(PlayerId.of(0));
    Game game = new Game(List.of(player1), null, testResultCardDeck);
    GameService gameService = new FakeGameService(game);

    SimpMessagingTemplate spySimpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    GameController gameController = new GameController(gameService, spySimpMessagingTemplate);

    gameController.handleDrawTestResultCard(String.valueOf(player1.id().getId()));

    ArgumentCaptor<DrawnTestResultCardEvent> captor = ArgumentCaptor.forClass(DrawnTestResultCardEvent.class);
    verify(spySimpMessagingTemplate).convertAndSend(any(), captor.capture());

    DrawnTestResultCardEvent cardEvent = captor.getValue();
    assertThat(cardEvent.getTestResultCardView().getId())
      .isEqualTo(1759);
    assertThat(cardEvent.getPlayerId())
      .isEqualTo("0");
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
    GameController gameController = new GameController(gameService, spySimpMessagingTemplate);

    gameController.handleDiscardTestResultCard(String.valueOf(player1.id().getId()));

    DiscardedTestResultCardEvent expectedEvent = new DiscardedTestResultCardEvent(player1.id());
    verify(spySimpMessagingTemplate).convertAndSend("/topic/testresultcard", expectedEvent);
  }
}
