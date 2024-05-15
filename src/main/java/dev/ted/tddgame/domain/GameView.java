package dev.ted.tddgame.domain;

public record GameView(String name, String handle, int playerCount) {
    public static GameView from(Game game) {
        return new GameView(game.name(), game.handle(), game.players().size());
    }
}
