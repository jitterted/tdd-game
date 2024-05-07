package com.jitterted.tddgame.adapter.vue.masterview;

import com.jitterted.tddgame.adapter.vue.CardsView;
import com.jitterted.tddgame.adapter.vue.TestResultCardView;
import com.jitterted.tddgame.domain.Deck;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.TestResultCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameMasterView {

    private final List<PlayerWorldView> players;
    private final TestResultCardView showingTestResultCard;
    private final CardDeckView playingCardDeck;
    private final CardDeckView testResultCardDeck;

    public GameMasterView(List<PlayerWorldView> players, TestResultCardView showingTestResultCard, CardDeckView playingCardDeck, CardDeckView testResultCardDeck) {
        this.players = players;
        this.showingTestResultCard = showingTestResultCard;
        this.playingCardDeck = playingCardDeck;
        this.testResultCardDeck = testResultCardDeck;
    }

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
        return testResultCardDiscardPile.getLast();
    }

    public List<PlayerWorldView> getPlayers() {
        return this.players;
    }

    public TestResultCardView getShowingTestResultCard() {
        return this.showingTestResultCard;
    }

    public CardDeckView getPlayingCardDeck() {
        return this.playingCardDeck;
    }

    public CardDeckView getTestResultCardDeck() {
        return this.testResultCardDeck;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameMasterView)) {
            return false;
        }
        final GameMasterView other = (GameMasterView) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$players = this.getPlayers();
        final Object other$players = other.getPlayers();
        if (this$players == null ? other$players != null : !this$players.equals(other$players)) {
            return false;
        }
        final Object this$showingTestResultCard = this.getShowingTestResultCard();
        final Object other$showingTestResultCard = other.getShowingTestResultCard();
        if (this$showingTestResultCard == null ? other$showingTestResultCard != null : !this$showingTestResultCard.equals(other$showingTestResultCard)) {
            return false;
        }
        final Object this$playingCardDeck = this.getPlayingCardDeck();
        final Object other$playingCardDeck = other.getPlayingCardDeck();
        if (this$playingCardDeck == null ? other$playingCardDeck != null : !this$playingCardDeck.equals(other$playingCardDeck)) {
            return false;
        }
        final Object this$testResultCardDeck = this.getTestResultCardDeck();
        final Object other$testResultCardDeck = other.getTestResultCardDeck();
        if (this$testResultCardDeck == null ? other$testResultCardDeck != null : !this$testResultCardDeck.equals(other$testResultCardDeck)) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GameMasterView;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $players = this.getPlayers();
        result = result * PRIME + ($players == null ? 43 : $players.hashCode());
        final Object $showingTestResultCard = this.getShowingTestResultCard();
        result = result * PRIME + ($showingTestResultCard == null ? 43 : $showingTestResultCard.hashCode());
        final Object $playingCardDeck = this.getPlayingCardDeck();
        result = result * PRIME + ($playingCardDeck == null ? 43 : $playingCardDeck.hashCode());
        final Object $testResultCardDeck = this.getTestResultCardDeck();
        result = result * PRIME + ($testResultCardDeck == null ? 43 : $testResultCardDeck.hashCode());
        return result;
    }

    public String toString() {
        return "GameMasterView(players=" + this.getPlayers() + ", showingTestResultCard=" + this.getShowingTestResultCard() + ", playingCardDeck=" + this.getPlayingCardDeck() + ", testResultCardDeck=" + this.getTestResultCardDeck() + ")";
    }
}
