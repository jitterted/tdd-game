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
import com.jitterted.tddgame.domain.TestResultCard;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class GameControllerDrawTest {

  @Test
  public void playerDrawActionResultsInNewCardDrawnToPlayerHand() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService);
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
  public void testResultDrawReturnsTitleOfTestResultCard() throws Exception {
    Deck<TestResultCard> testResultCardDeck = new Deck<>(new CopyCardShuffler<>());
    TestResultCard testResultCard = new TestResultCard(CardId.of(1759), "As Predicted");
    testResultCardDeck.addToDrawPile(testResultCard);

    Game game = new Game(Collections.emptyList(), null, testResultCardDeck);
    GameService gameService = new FakeGameService(game);

    GameController gameController = new GameController(gameService);

    TestResultCardView testResultCardView = gameController.handleDrawTestResultCard("0");

    assertThat(testResultCardView.getId())
      .isEqualTo(testResultCard.id().getId());
    assertThat(testResultCardDeck.discardPile())
      .containsOnly(testResultCard);
  }

}
