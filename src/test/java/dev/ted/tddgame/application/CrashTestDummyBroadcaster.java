package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;

class CrashTestDummyBroadcaster implements Broadcaster {
    @Override
    public void announcePlayerConnectedToGame(Game game, Player player) {
        throw new IllegalStateException("announcePlayerConnectedToGame should NOT have been invoked.");
    }

    @Override
    public void prepareForGamePlay(Game game) {
        throw new IllegalArgumentException("clearStartGameModal should NOT have been invoked.");
    }

    @Override
    public void gameUpdate(Game game) {
        throw new IllegalArgumentException("gameUpdate should NOT have been invoked.");
    }
}
