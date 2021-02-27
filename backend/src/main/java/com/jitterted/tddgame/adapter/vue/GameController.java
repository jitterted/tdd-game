package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.User;
import com.jitterted.tddgame.domain.port.GameStateChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game/players")
public class GameController {

  private final GameService gameService;
  private final GameStateChannel gameStateChannel;

  @Autowired
  public GameController(GameService gameService, GameStateChannel gameStateChannel) {
    this.gameService = gameService;
    this.gameStateChannel = gameStateChannel;
  }

  @PostMapping
  public PlayerIdDto connectUserToPlayerInGame(@RequestBody UserDto user) {
    System.out.println("Received connect for user: " + user.getUserName());
    Player assignedPlayer = gameService.assignNextAvailablePlayerToUser(new User(user.getUserName()));

    gameStateChannel.playerActed(gameService.currentGame());

    return PlayerIdDto.from(assignedPlayer.id());
  }

  @GetMapping("{playerId}")
  public PlayerGameView playerGameView(@PathVariable("playerId") String playerIdString) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));
    return PlayerGameView.from(gameService.currentGame(), playerId);
  }

  @PostMapping("{playerId}/discards")
  public void discard(@PathVariable("playerId") String playerIdString,
                      @RequestBody DiscardAction discardAction) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));

    discardAction.executeFor(playerId, gameService);

    gameStateChannel.playerActed(gameService.currentGame());
  }

  @PostMapping("{playerId}/plays")
  public void playCard(@PathVariable("playerId") String playerIdString,
                       @RequestBody PlayCardAction playCardAction) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));

    gameService.currentGame().playCardFor(playerId, CardId.of(playCardAction.getId()));

    gameStateChannel.playerActed(gameService.currentGame());
  }

  @PostMapping("{playerId}/actions")
  public void handleAction(@PathVariable("playerId") String playerIdString,
                           @RequestBody PlayerAction playerAction) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));

    playerAction.executeFor(playerId, gameService);

    gameStateChannel.playerActed(gameService.currentGame());
  }

  @PostMapping("{playerId}/test-result-card-draws")
  public ResponseEntity<Void> handleDrawTestResultCard(@PathVariable("playerId") String playerIdString) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));
    Game game = gameService.currentGame();
    game.drawTestResultCardFor(playerId);

    gameStateChannel.testResultCardDrawn(game.drawnTestResultCard());

    return ResponseEntity.noContent().build();
  }

  @PostMapping("{playerId}/test-result-card-discards")
  public ResponseEntity<Void> handleDiscardTestResultCard(@PathVariable("playerId") String playerIdString) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));
    Game game = gameService.currentGame();
    game.discardTestResultCardFor(playerId);

    gameStateChannel.testResultCardDiscarded(playerId);

    return ResponseEntity.noContent().build();
  }

}
