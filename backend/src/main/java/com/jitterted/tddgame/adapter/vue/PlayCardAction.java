package com.jitterted.tddgame.adapter.vue;

public class PlayCardAction {
  private int id;

  public PlayCardAction() {
  }

  public PlayCardAction(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PlayCardAction)) {
      return false;
    }
    final PlayCardAction other = (PlayCardAction) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    if (this.getId() != other.getId()) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof PlayCardAction;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + this.getId();
    return result;
  }

  public String toString() {
    return "PlayCardAction(id=" + this.getId() + ")";
  }
}
