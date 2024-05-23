package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.Game;

import java.util.List;

public record GameLobbyView(String name, String handle, int playerCount) {
    public static GameLobbyView from(Game game) {
        return new GameLobbyView(game.name(), game.handle(), game.players().size());
    }

    public static List<GameLobbyView> from(List<Game> games) {
        return games.stream().map(GameLobbyView::from).toList();
    }
}
