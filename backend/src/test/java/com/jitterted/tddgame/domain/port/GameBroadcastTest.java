package com.jitterted.tddgame.domain.port;

import com.jitterted.tddgame.domain.CardFactory;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.DefaultDeckFactory;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerFactory;
import com.jitterted.tddgame.domain.PlayingCard;
import com.jitterted.tddgame.domain.TestResultCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class GameBroadcastTest {

  @Test
  public void discardFromHandResultsInGameStateChangedEventSent() throws Exception {
    GameStateChannel gameStateChannelSpy = spy(GameStateChannel.class);
    DefaultDeckFactory deckFactory = new DefaultDeckFactory(new CardFactory());
    Deck<TestResultCard> testResultCardDeck = deckFactory.createTestResultCardDeck();
    Deck<PlayingCard> playerCardDeck = deckFactory.createPlayingCardDeck();
    List<Player> playerList = new PlayerFactory().createTwoPlayers();
    Game game = new Game(playerList, playerCardDeck, testResultCardDeck, gameStateChannelSpy);
    game.start();

    Player player = playerList.get(0);
    PlayingCard cardToDiscard = player.hand().cards().get(0);

    game.discardFromHand(player.id(), cardToDiscard.id());

    verify(gameStateChannelSpy).playerActed(any(Game.class));
  }

}
