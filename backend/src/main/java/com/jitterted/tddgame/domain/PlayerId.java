package com.jitterted.tddgame.domain;

public class PlayerId {
  private final int id;

  public PlayerId(int id) {
    this.id = id;
  }

  public static PlayerId of(int id) {
    return new PlayerId(id);
  }

  public int getId() {
    return this.id;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PlayerId)) {
      return false;
    }
    final PlayerId other = (PlayerId) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    if (this.getId() != other.getId()) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof PlayerId;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + this.getId();
    return result;
  }

  public String toString() {
    return "PlayerId(id=" + this.getId() + ")";
  }
}
