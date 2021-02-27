package com.jitterted.tddgame.adapter.broadcast;

import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameFactory;
import com.jitterted.tddgame.domain.Location;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerId;
import com.jitterted.tddgame.domain.PlayingCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Disabled("until refactoring of websocket channel is complete")
public class LocationTest {

  @Test
  public void newGameThenPlayer1IsAtLocationOne() throws Exception {
    Game game = new GameFactory().createTwoPlayerGame();

    Location location = game.locationFor(PlayerId.of(0));

    assertThat(location)
      .isEqualTo(Location.ONE);
  }

  @Test
  public void player1AtLocation1DiscardsCardThenIsAtLocation2() throws Exception {
    // given
    Game game = new GameFactory().createTwoPlayerGame();

    // when
    // player 1 discards a card from their hand
    Player player1 = game.playerFor(PlayerId.of(0));
    PlayingCard cardToDiscard = player1.hand().cards().get(0);
    game.discardFromHand(PlayerId.of(0), cardToDiscard.id());

    // then
    // message is broadcast saying location for player 1 is now LocationTWO
    // verify via Game invoking some method on the GameStateChannel (via Spy)

  }

}
