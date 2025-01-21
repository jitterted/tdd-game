package dev.ted.tddgame.application;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.ForTrackingPlayerMessageSenders;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;

// USE CASE: Handles players connecting to the game
public class PlayerConnector {
    private final Broadcaster broadcaster;
    private final MemberFinder memberFinder;
    private final GameFinder gameFinder;
    private final ForTrackingPlayerMessageSenders trackingPlayerMessageSenders;

    public PlayerConnector(Broadcaster broadcaster,
                           MemberFinder memberFinder,
                           GameFinder gameFinder,
                           ForTrackingPlayerMessageSenders trackingPlayerMessageSenders) {
        this.broadcaster = broadcaster;
        this.memberFinder = memberFinder;
        this.gameFinder = gameFinder;
        this.trackingPlayerMessageSenders = trackingPlayerMessageSenders;
    }

    public void connect(String memberUsername, String gameHandle, MessageSender messageSender) {
        Game game = gameFinder.byHandle(gameHandle);
        MemberId memberId = memberFinder.byUsername(memberUsername).id();
        // ASSERT is this player still in the game? they should be!
        Player player = game.playerFor(memberId);
        trackingPlayerMessageSenders.add(messageSender, gameHandle, player.id());
        switch (game.state()) {
            case CREATED -> {
                broadcaster.announcePlayerConnectedToGame(game, player);
            }
            case IN_PROGRESS -> {
                broadcaster.prepareForGamePlay(game);
                broadcaster.gameUpdate(game);
            }

        }
    }
}
