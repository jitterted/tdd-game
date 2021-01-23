package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.DummyCardShuffler;
import com.jitterted.tddgame.domain.FakeGameService;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.GameStateChannel;
import com.jitterted.tddgame.domain.OnDrawGoesTo;
import com.jitterted.tddgame.domain.OnPlayGoesTo;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayingCard;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameControllerDiscardsTest {

  @Test
  public void discardFromHandIsTransferredToDeckDiscardPile() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService, mock(GameStateChannel.class));
    Game game = gameService.currentGame();
    Deck<PlayingCard> deck = game.deck();
    Player player = game.players().get(0);
    PlayingCard playingCardFromHand = player.hand().cards().get(0);

    gameController.discard(String.valueOf(player.id().getId()),
                           new DiscardAction(playingCardFromHand.id().getId(), DiscardAction.SOURCE_HAND));

    assertThat(deck.discardPile())
      .containsExactly(playingCardFromHand);
  }

  @Test
  public void discardFromHandResultsInGameStateChangedEventSent() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());

    SimpMessagingTemplate spySimpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    GameStateChannel gameStateChannel = new StompGameStateChannel(spySimpMessagingTemplate, new SyncTaskExecutor());
    GameController gameController = new GameController(gameService, gameStateChannel);

    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    PlayingCard playingCardFromHand = player.hand().cards().get(0);

    gameController.discard(String.valueOf(player.id().getId()),
                           new DiscardAction(playingCardFromHand.id().getId(), DiscardAction.SOURCE_HAND));

    GameStateChangedEvent expectedEvent = GameStateChangedEvent.from(game);
    verify(spySimpMessagingTemplate).convertAndSend(eq("/topic/gamestate"), eq(expectedEvent), anyMap());
  }

  @Test
  public void discardFromInPlayIsTransferredToDeckDiscardPile() throws Exception {
    Deck<PlayingCard> deck = new Deck<>(new DummyCardShuffler<>());
    Game game = new Game(new PlayerFactory().createTwoPlayers(), deck, null);
    GameService gameService = new FakeGameService(game);
    GameController gameController = new GameController(gameService, mock(GameStateChannel.class));

    PlayingCard playableCard = new CardFactory().playingCard("playable", OnPlayGoesTo.SELF, OnDrawGoesTo.HAND);
    Player player = game.players().get(0);
    player.inPlay().add(playableCard);

    gameController.discard(String.valueOf(player.id().getId()),
                           new DiscardAction(playableCard.id().getId(), DiscardAction.SOURCE_IN_PLAY));

    assertThat(deck.discardPile())
      .containsExactly(playableCard);
  }

}
