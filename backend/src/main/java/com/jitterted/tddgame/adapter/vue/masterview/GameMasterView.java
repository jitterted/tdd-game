package com.jitterted.tddgame.adapter.vue.masterview;

import com.jitterted.tddgame.adapter.vue.CardsView;
import com.jitterted.tddgame.adapter.vue.TestResultCardView;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.TestResultCard;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameMasterView {

  private final List<PlayerWorldView> players;
  private final TestResultCardView showingTestResultCard;
  private final CardDeckView playingCardDeck;
  private final CardDeckView testResultCardDeck;

  public static GameMasterView from(Game game) {
    List<PlayerWorldView> playerWorldViews = new ArrayList<>();
    for (Player player : game.players()) {
      CardsView handView = CardsView.from(player.hand().cards());
      CardsView inPlayView = CardsView.from(player.inPlay().cards());
      String name = player.assignedUser() == null ? "nobody" : player.assignedUser().getName();
      PlayerWorldView playerWorldView = new PlayerWorldView(name, handView, inPlayView);
      playerWorldViews.add(playerWorldView);
    }

    Deck<TestResultCard> testResultCardDeck = game.testResultCardDeck();
    List<TestResultCard> testResultCardDiscardPile = testResultCardDeck.discardPile();
    return new GameMasterView(
      playerWorldViews,
      TestResultCardView.of(mostRecentlyDiscardedCardFrom(testResultCardDiscardPile)),
      fromDeck(game.deck()),
      fromDeck(testResultCardDeck)
    );
  }

  @NotNull
  private static <C> CardDeckView fromDeck(Deck<C> deck) {
    return new CardDeckView(deck.discardPile().size(), deck.drawPileSize());
  }

  private static TestResultCard mostRecentlyDiscardedCardFrom(List<TestResultCard> testResultCardDiscardPile) {
    return testResultCardDiscardPile.get(testResultCardDiscardPile.size() - 1);
  }

}
