package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;

public interface Broadcaster {
    void announcePlayerConnectedToGame(Game game, Player player);

    void clearStartgameModal(Game game);
}
