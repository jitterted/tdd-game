package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.GameStateChannel;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.PlayingCard;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import com.jitterted.tddgame.domain.Usage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GameControllerTest {

  @Test
  public void newGameIsFullHandDealtToAllPlayers() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    Game game = gameService.currentGame();
    Player player = game.players().get(0);

    GameController gameController = new GameController(gameService, null);

    PlayerGameView playerGameView = gameController.playerGameView(String.valueOf(player.id().getId()));

    assertThat(playerGameView.getHand().getCards())
      .hasSize(5);
  }

  @Test
  public void selfPlayedCardIsTransferredFromHandToInPlay() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    PlayingCard playingCardFromHand = new PlayingCard(CardId.of(999), "played card", Usage.SELF);
    player.hand().add(playingCardFromHand);

    GameStateChannel dummyGameStateChannel = mock(GameStateChannel.class);
    GameController gameController = new GameController(gameService, dummyGameStateChannel);

    gameController.playCard(String.valueOf(player.id().getId()),
                            new PlayCardAction(playingCardFromHand.id().getId()));

    assertThat(player.inPlay().cards())
      .contains(playingCardFromHand);
  }

  @Test
  public void gameViewEventSentWhenCardIsPlayedByPlayer() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    Game game = gameService.currentGame();
    Player player1 = game.players().get(0);
    PlayingCard playingCardFromHand = player1.hand().cards().get(0);

    SimpMessagingTemplate spySimpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    GameStateChannel gameStateChannel = new StompGameStateChannel(spySimpMessagingTemplate);
    GameController gameController = new GameController(gameService, gameStateChannel);

    PlayCardAction playCardAction = new PlayCardAction(playingCardFromHand.id().getId());
    gameController.playCard(String.valueOf(player1.id().getId()), playCardAction);

    GameStateChangedEvent expectedEvent = GameStateChangedEvent.from(game);
    verify(spySimpMessagingTemplate).convertAndSend(eq("/topic/gamestate"), eq(expectedEvent), anyMap());
  }

  @Test
  public void connectUserIsAssignedToPlayer() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService, null);
    UserDto userDto = new UserDto("tba");

    PlayerIdDto playerIdDto = gameController.connectUserToPlayerInGame(userDto);

    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdDto.getPlayerId()));
    Game game = gameService.currentGame();
    Player assignedPlayer = game.playerFor(playerId);

    assertThat(assignedPlayer.assignedUser().getName())
      .isEqualTo("tba");
  }

}
