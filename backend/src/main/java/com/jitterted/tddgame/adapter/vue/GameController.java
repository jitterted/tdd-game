package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

  private final GameService gameService;

  @Autowired
  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("game")
  public PlayerGameView playerGameView() {
    return PlayerGameView.from(gameService.currentGame());
  }

  @PostMapping("game/player/{playerId}/discards")
  public void discard(@PathVariable("playerId") String playerIdString,
                      @RequestBody DiscardAction discardAction) {
    int playerId = Integer.parseInt(playerIdString);
    discardAction.executeFor(playerId, gameService);
  }

  @PostMapping("game/player/{playerId}/plays")
  public void playCard(@PathVariable("playerId") String playerIdString,
                       @RequestBody PlayCardAction playCardAction) {
    int playerId = Integer.parseInt(playerIdString);
    gameService.currentGame().playCardFor(PlayerId.of(playerId), CardId.of(playCardAction.getId()));
  }

  @PostMapping("game/player/{playerId}/actions")
  public void handleAction(@PathVariable("playerId") String playerIdString,
                           @RequestBody PlayerAction playerAction) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));
    playerAction.executeFor(playerId, gameService);
  }
}
