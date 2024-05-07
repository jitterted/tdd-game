package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerId;

public class PlayerAction {
  public static final String DRAW_CARD = "DRAW_CARD";
  public static final String DRAW_HAND = "DRAW_HAND";

  private String action;

  public PlayerAction(String action) {
    this.action = action;
  }

  public PlayerAction() {
  }

  public void executeFor(PlayerId playerId, GameService gameService) {
    switch (action.toUpperCase()) {
      case DRAW_CARD -> gameService.currentGame().drawCardFor(playerId);
      case DRAW_HAND -> gameService.currentGame().drawToFullHandFor(playerId);
    }
  }

  public String getAction() {
    return this.action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PlayerAction)) {
      return false;
    }
    final PlayerAction other = (PlayerAction) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    final Object this$action = this.getAction();
    final Object other$action = other.getAction();
    if (this$action == null ? other$action != null : !this$action.equals(other$action)) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof PlayerAction;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $action = this.getAction();
    result = result * PRIME + ($action == null ? 43 : $action.hashCode());
    return result;
  }

  public String toString() {
    return "PlayerAction(action=" + this.getAction() + ")";
  }
}
