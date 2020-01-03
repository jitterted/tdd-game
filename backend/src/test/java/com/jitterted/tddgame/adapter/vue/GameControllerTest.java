package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Hand;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.TwoPlayerGameService;
import com.jitterted.tddgame.domain.Usage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameControllerTest {

  @Test
  public void newGameIsFullHandDealtToAllPlayers() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    Game game = gameService.currentGame();
    Player player = game.players().get(0);

    GameController gameController = new GameController(gameService);

    PlayerGameView playerGameView = gameController.playerGameView(String.valueOf(player.id().getId()));

    assertThat(playerGameView.getHand().getCards())
      .hasSize(5);
  }

  @Test
  public void selfPlayedCardIsTransferredFromHandToInPlay() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    Card cardFromHand = new Card(CardId.of(999), "played card", Usage.SELF);
    player.hand().add(cardFromHand);

    GameController gameController = new GameController(gameService);

    gameController.playCard(String.valueOf(player.id().getId()),
                            new PlayCardAction(cardFromHand.id().getId()));

    assertThat(player.inPlay().cards())
      .contains(cardFromHand);
  }

  @Test
  public void playerDrawActionResultsInNewCardDrawnToPlayerHand() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService);
    Game game = gameService.currentGame();
    Player player = game.players().get(0);
    Hand hand = player.hand();
    CardId cardId = hand.cards().get(0).id();
    hand.remove(cardId); // make room in hand for draw from deck

    gameController.handleAction(playerIdStringFrom(player), new PlayerAction(PlayerAction.DRAW_CARD));

    assertThat(player.hand().isFull())
      .isTrue();
  }

  @Test
  public void connectUserIsAssignedToPlayer() throws Exception {
    GameService gameService = new TwoPlayerGameService(new PlayerFactory());
    GameController gameController = new GameController(gameService);
    UserDto userDto = new UserDto("tba");

    PlayerIdDto playerIdDto = gameController.connectUserToPlayerInGame(userDto);

    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdDto.getPlayerId()));
    Game game = gameService.currentGame();
    Player assignedPlayer = game.playerFor(playerId);

    assertThat(assignedPlayer.assignedUser().getName())
      .isEqualTo("tba");
  }

  private String playerIdStringFrom(Player player) {
    return String.valueOf(player.id().getId());
  }

}
