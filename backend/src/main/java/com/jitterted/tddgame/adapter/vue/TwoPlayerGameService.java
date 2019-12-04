package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.DeckFactory;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameFactory;
import com.jitterted.tddgame.domain.GameService;
import org.springframework.stereotype.Service;

@Service
public class TwoPlayerGameService implements GameService {
  @Override
  public Game currentGame() {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory).createTwoPlayerGame();
    game.start();
    return game;
  }
}
