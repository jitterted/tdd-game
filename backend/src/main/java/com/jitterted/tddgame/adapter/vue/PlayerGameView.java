package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.PlayerId;

public class PlayerGameView {
  private final CardsView hand;
  private final CardsView inPlay;
  private final CardsView opponentInPlay;
  private final PlayerView player;
  private final PlayerView opponent;

  public PlayerGameView(CardsView hand, CardsView inPlay, CardsView opponentInPlay, PlayerView player, PlayerView opponent) {
    this.hand = hand;
    this.inPlay = inPlay;
    this.opponentInPlay = opponentInPlay;
    this.player = player;
    this.opponent = opponent;
  }

  public static PlayerGameView from(Game game, PlayerId playerId) {
    Player player = game.playerFor(playerId);
    CardsView handView = CardsView.from(player.hand().cards());
    CardsView inPlayView = CardsView.from(player.inPlay().cards());

    Player opponent = game.opponentFor(player);
    CardsView opponentInPlayView = CardsView.from(opponent.inPlay().cards());

    PlayerView playerView = PlayerView.from(player);
    PlayerView opponentPlayerView = PlayerView.from(opponent);

    return new PlayerGameView(handView,
                              inPlayView,
                              opponentInPlayView,
                              playerView,
                              opponentPlayerView
    );
  }

  public CardsView getHand() {
    return this.hand;
  }

  public CardsView getInPlay() {
    return this.inPlay;
  }

  public CardsView getOpponentInPlay() {
    return this.opponentInPlay;
  }

  public PlayerView getPlayer() {
    return this.player;
  }

  public PlayerView getOpponent() {
    return this.opponent;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PlayerGameView)) {
      return false;
    }
    final PlayerGameView other = (PlayerGameView) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    final Object this$hand = this.getHand();
    final Object other$hand = other.getHand();
    if (this$hand == null ? other$hand != null : !this$hand.equals(other$hand)) {
      return false;
    }
    final Object this$inPlay = this.getInPlay();
    final Object other$inPlay = other.getInPlay();
    if (this$inPlay == null ? other$inPlay != null : !this$inPlay.equals(other$inPlay)) {
      return false;
    }
    final Object this$opponentInPlay = this.getOpponentInPlay();
    final Object other$opponentInPlay = other.getOpponentInPlay();
    if (this$opponentInPlay == null ? other$opponentInPlay != null : !this$opponentInPlay.equals(other$opponentInPlay)) {
      return false;
    }
    final Object this$player = this.getPlayer();
    final Object other$player = other.getPlayer();
    if (this$player == null ? other$player != null : !this$player.equals(other$player)) {
      return false;
    }
    final Object this$opponent = this.getOpponent();
    final Object other$opponent = other.getOpponent();
    if (this$opponent == null ? other$opponent != null : !this$opponent.equals(other$opponent)) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof PlayerGameView;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $hand = this.getHand();
    result = result * PRIME + ($hand == null ? 43 : $hand.hashCode());
    final Object $inPlay = this.getInPlay();
    result = result * PRIME + ($inPlay == null ? 43 : $inPlay.hashCode());
    final Object $opponentInPlay = this.getOpponentInPlay();
    result = result * PRIME + ($opponentInPlay == null ? 43 : $opponentInPlay.hashCode());
    final Object $player = this.getPlayer();
    result = result * PRIME + ($player == null ? 43 : $player.hashCode());
    final Object $opponent = this.getOpponent();
    result = result * PRIME + ($opponent == null ? 43 : $opponent.hashCode());
    return result;
  }

  public String toString() {
    return "PlayerGameView(hand=" + this.getHand() + ", inPlay=" + this.getInPlay() + ", opponentInPlay=" + this.getOpponentInPlay() + ", player=" + this.getPlayer() + ", opponent=" + this.getOpponent() + ")";
  }
}
