package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.CopyCardShuffler;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.DrawnTestResultCard;
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
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

    SimpMessagingTemplate dummySimpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    GameController gameController = new GameController(gameService, dummySimpMessagingTemplate);

    gameController.handleDrawTestResultCard(String.valueOf(player1.id().getId()));

    DrawnTestResultCard drawnTestResultCard = new DrawnTestResultCard(testResultCard, player1);
    assertThat(game.drawnTestResultCard())
      .isEqualTo(drawnTestResultCard);
  }

}
