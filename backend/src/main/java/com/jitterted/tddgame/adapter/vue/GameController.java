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
  public void discardFromHand(@PathVariable("playerId") String playerIdString,
                              @RequestBody CardIdDto cardId) {
    int playerId = Integer.parseInt(playerIdString);
    gameService.currentGame().discard(PlayerId.of(playerId), CardId.of(cardId.getId()));
  }

  @PostMapping("game/player/{playerId}/actions")
  public void handleAction(@PathVariable("playerId") String playerIdString,
                           @RequestBody PlayerAction playerAction) {
    PlayerId playerId = PlayerId.of(Integer.parseInt(playerIdString));

  }

}
