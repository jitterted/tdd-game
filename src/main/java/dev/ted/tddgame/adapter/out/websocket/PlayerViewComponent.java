package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

public class PlayerViewComponent {

    public String generateHtmlFor(Player player) {
        return player.playerName();
    }
}
