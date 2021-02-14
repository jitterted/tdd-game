package com.jitterted.tddgame.adapter.broadcast;

import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.DeckFactory;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameFactory;
import com.jitterted.tddgame.domain.Location;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LocationTest {

  @Test
  public void newGameThenPlayer1IsAtLocationOne() throws Exception {
    DeckFactory deckFactory = new DeckFactory(new CardFactory());
    Game game = new GameFactory(deckFactory, new PlayerFactory()).createTwoPlayerGame();

    Location location = game.locationFor(PlayerId.of(1));

    assertThat(location)
      .isEqualTo(Location.ONE);
  }

  @Test
  public void player1AtLocation1DiscardsCardThenIsAtLocation2() throws Exception {
    // given
    // new game, player 1 is at LocationONE
    // when
    // player 1 discards a card from their hand
    // then
    // message is broadcast saying location for player 1 is now LocationTWO
    // verify via Game invoking some method on the GameStateChannel (via Spy)
  }

}
