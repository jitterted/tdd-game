package dev.ted.tddgame.application;

import dev.ted.tddgame.adapter.shared.MessageSender;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.ForTrackingPlayerMessageSenders;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// USE CASE: Handles players connecting to the game
public class PlayerConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerConnector.class);
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
        LOGGER.info("Connecting to game '{}' with member '{}' - {}", gameHandle, memberUsername, memberId);
        Player player;
        try {
            player = game.playerFor(memberId);
        } catch (IllegalStateException ise) {
            LOGGER.info("Sending script to kick client back to the Lobby...", ise);
            messageSender.sendMessage("<swap id='other-players'><script>document.location.href='/lobby';</script></swap>");
            return;
        }
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
