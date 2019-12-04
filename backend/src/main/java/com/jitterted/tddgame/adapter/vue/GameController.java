package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

}
