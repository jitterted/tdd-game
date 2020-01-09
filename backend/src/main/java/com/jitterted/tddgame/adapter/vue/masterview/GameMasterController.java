package com.jitterted.tddgame.adapter.vue.masterview;

import com.jitterted.tddgame.domain.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameMasterController {
  private final GameService gameService;

  @Autowired
  public GameMasterController(GameService gameService) {
    this.gameService = gameService;
  }


  @GetMapping
  public GameMasterView viewGame() {
    return GameMasterView.from(gameService.currentGame());
  }

}
