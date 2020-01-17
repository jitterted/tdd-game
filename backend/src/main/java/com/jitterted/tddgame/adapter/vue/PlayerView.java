package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Player;
import lombok.Data;

@Data
public class PlayerView {
  private final String name;
  private final String id;

  public static PlayerView from(Player player) {
    String playerName = player.assignedUser() == null ? "nobody" : player.assignedUser().getName();
    return new PlayerView(playerName, String.valueOf(player.id().getId()));
  }
}
