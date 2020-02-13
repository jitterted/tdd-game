package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.adapter.vue.masterview.PlayerWorldView;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GameStateChangedEvent {

  private final Map<String, PlayerWorldView> players;

  public static GameStateChangedEvent from(Game game) {
    Map<String, PlayerWorldView> playerWorldViews = new HashMap<>();
    for (Player player : game.players()) {
      CardsView handView = CardsView.from(player.hand().cards());
      CardsView inPlayView = CardsView.from(player.inPlay().cards());
      String name = player.assignedUser() == null ? "nobody" : player.assignedUser().getName();
      PlayerWorldView playerWorldView = new PlayerWorldView(name, handView, inPlayView);
      playerWorldViews.put(String.valueOf(player.id().getId()), playerWorldView);
    }

    return new GameStateChangedEvent(playerWorldViews);
  }

}
