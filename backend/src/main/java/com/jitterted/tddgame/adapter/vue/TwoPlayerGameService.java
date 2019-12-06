package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.DeckFactory;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameFactory;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwoPlayerGameService implements GameService {
  private Game game;

  public TwoPlayerGameService() {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();
    game.start();
  }

  @Override
  public Game currentGame() {
    return game;
  }
}
