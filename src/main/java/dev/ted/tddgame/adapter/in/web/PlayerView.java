package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.Player;

import java.util.List;

public record PlayerView(String name) {
    static List<PlayerView> from(List<Player> players) {
        return players.stream()
                      .map(player -> new PlayerView(player.playerName()))
                      .toList();
    }
}
