package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Player;

public class PlayerView {
  private final String name;
  private final String id;

  public PlayerView(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public static PlayerView from(Player player) {
    String playerName = player.assignedUser() == null ? "nobody" : player.assignedUser().getName();
    return new PlayerView(playerName, String.valueOf(player.id().getId()));
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PlayerView)) {
      return false;
    }
    final PlayerView other = (PlayerView) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    final Object this$name = this.getName();
    final Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
      return false;
    }
    final Object this$id = this.getId();
    final Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof PlayerView;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    return result;
  }

  public String toString() {
    return "PlayerView(name=" + this.getName() + ", id=" + this.getId() + ")";
  }
}
