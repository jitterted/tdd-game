package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;

// USE CASE: Handles players connecting to the game
public class PlayerConnector {
    private final Broadcaster broadcaster;
    private final MemberFinder memberFinder;
    private final GameFinder gameFinder;

    public PlayerConnector(Broadcaster broadcaster,
                           MemberFinder memberFinder,
                           GameFinder gameFinder) {
        this.broadcaster = broadcaster;
        this.memberFinder = memberFinder;
        this.gameFinder = gameFinder;
    }

    public void connect(String playerUsername, String gameHandle) {
        Game game = gameFinder.byHandle(gameHandle);
        MemberId memberId = memberFinder.byUsername(playerUsername).id();
        Player player = game.playerFor(memberId);
        // MessageSendersForPlayers.add(messageSender, gameHandle, player.id())
        broadcaster.announcePlayerConnectedToGame(game, player);
    }
}
