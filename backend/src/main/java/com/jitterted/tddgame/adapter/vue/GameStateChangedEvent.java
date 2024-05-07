package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.adapter.vue.masterview.PlayerWorldView;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.Player;

import java.util.HashMap;
import java.util.Map;

public class GameStateChangedEvent {

    private final Map<String, PlayerWorldView> players;

    public GameStateChangedEvent(Map<String, PlayerWorldView> players) {
        this.players = players;
    }

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

    public Map<String, PlayerWorldView> getPlayers() {
        return this.players;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameStateChangedEvent)) {
            return false;
        }
        final GameStateChangedEvent other = (GameStateChangedEvent) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$players = this.getPlayers();
        final Object other$players = other.getPlayers();
        if (this$players == null ? other$players != null : !this$players.equals(other$players)) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GameStateChangedEvent;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $players = this.getPlayers();
        result = result * PRIME + ($players == null ? 43 : $players.hashCode());
        return result;
    }

    public String toString() {
        return "GameStateChangedEvent(players=" + this.getPlayers() + ")";
    }
}
