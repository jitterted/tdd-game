package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerId;
import org.slf4j.Logger;

public class DiscardAction {
  public static final String SOURCE_HAND = "hand";
  public static final String SOURCE_IN_PLAY = "in-play";
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(DiscardAction.class);

  private int id;
  private String source;

  public DiscardAction() {
  }

  public DiscardAction(int id, String source) {
    this.id = id;
    this.source = source;
  }

  public void executeFor(PlayerId playerId, GameService gameService) {
    log.debug("Discard Request for Card {}, Source {}, from Player {}", id, source, playerId.getId());
    if (source.equals(SOURCE_HAND)) {
      gameService.currentGame().discardFromHand(playerId, CardId.of(id));
    } else if (source.equals(SOURCE_IN_PLAY)) {
      gameService.currentGame().discardFromInPlay(playerId, CardId.of(id));
    } else {
      throw new UnsupportedOperationException("Unknown source for Discard = \"" + source + "\", id = " + id);
    }
  }

  public int getId() {
    return this.id;
  }

  public String getSource() {
    return this.source;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof DiscardAction)) {
      return false;
    }
    final DiscardAction other = (DiscardAction) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    if (this.getId() != other.getId()) {
      return false;
    }
    final Object this$source = this.getSource();
    final Object other$source = other.getSource();
    if (this$source == null ? other$source != null : !this$source.equals(other$source)) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof DiscardAction;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + this.getId();
    final Object $source = this.getSource();
    result = result * PRIME + ($source == null ? 43 : $source.hashCode());
    return result;
  }

  public String toString() {
    return "DiscardAction(id=" + this.getId() + ", source=" + this.getSource() + ")";
  }
}
