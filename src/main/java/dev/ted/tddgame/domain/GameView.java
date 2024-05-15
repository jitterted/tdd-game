package dev.ted.tddgame.domain;

import java.util.List;

public record GameView(String name, String handle, int playerCount) {
    public static GameView from(Game game) {
        return new GameView(game.name(), game.handle(), game.players().size());
    }

    public static List<GameView> from(List<Game> games) {
        return games.stream().map(GameView::from).toList();
    }
}
