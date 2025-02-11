package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class Player {
    private static final EventEnqueuer DUMMY_EVENT_ENQUEUER = _ -> {};
    static final int PLAYER_HAND_FULL_SIZE = 5;
    private final PlayerId playerId;
    private final MemberId memberId;
    private final String playerName;
    private final List<ActionCard> hand = new ArrayList<>();
    private EventEnqueuer eventEnqueuer;
    private final Workspace workspace;

    public Player(PlayerId playerId, MemberId memberId,
                  String playerName, EventEnqueuer eventEnqueuer,
                  Workspace workspace) {
        this.playerId = playerId;
        this.memberId = memberId;
        this.playerName = playerName;
        this.eventEnqueuer = eventEnqueuer;
        this.workspace = workspace;
    }

    public static Player createNull(long id, String playerName) {
        PlayerId playerId = new PlayerId(id);
        Player player = new Player(playerId, new MemberId(42L), playerName,
                                   DUMMY_EVENT_ENQUEUER,
                                   new Workspace(playerId));
        // ensure that player events (from commands) get applied
        player.eventEnqueuer = gameEvent -> player.apply((PlayerEvent)gameEvent);
        return player;
    }

    public PlayerId id() {
        return playerId;
    }

    public MemberId memberId() {
        return memberId;
    }

    public Workspace workspace() {
        return workspace;
    }

    public String playerName() {
        return playerName;
    }

    public Stream<ActionCard> hand() {
        return hand.stream();
    }

    public void apply(PlayerEvent event) {
        switch (event) {
            case PlayerDrewActionCard(_, ActionCard actionCard) ->
                    hand.add(actionCard);

            case PlayerDiscardedActionCard(_, ActionCard actionCard) -> {
                hand.remove(actionCard);
                workspace.cardDiscarded();
            }

            case PlayerPlayedActionCard(_, ActionCard actionCard) -> {
                hand.remove(actionCard);
                workspace.cardPlayed(actionCard);
            }

            case PlayerDrewTechNeglectCard(_, ActionCard actionCard) -> {
                workspace.techNeglectCardPlayed(actionCard);
            }
        }
    }


    void drawCardFrom(ActionCardDeck actionCardDeck) {
        ensureHandNotFull();

        ActionCard drawnCard = actionCardDeck.draw();

        PlayerEvent event = drawnCard.drawnCardEventFor(memberId);

        eventEnqueuer.enqueue(event);

        assert hand.size() <= PLAYER_HAND_FULL_SIZE; // post-condition
    }

    private void ensureHandNotFull() {
        if (handIsFull()) {
            throw new HandAlreadyFull("Can't draw any more cards, the Hand is full with five cards");
        }
    }

    private boolean handIsFull() {
        return hand.size() == PLAYER_HAND_FULL_SIZE;
    }

    void discard(ActionCard actionCardToDiscard, ActionCardDeck actionCardDeck) {
        // check constraint: actionCardToDiscard MUST be in the Player's Hand
        PlayerEvent playerEvent =
                new PlayerDiscardedActionCard(memberId, actionCardToDiscard);
        eventEnqueuer.enqueue(playerEvent);
        actionCardDeck.acceptDiscard(actionCardToDiscard);
    }

    public void playCard(ActionCard actionCardToPlay) {
        // check constraint: actionCardToPlay MUST be in the Player's Hand
        // check constraint: must check with Workspace to decide if this is allowed
        PlayerEvent playerEvent =
                new PlayerPlayedActionCard(memberId, actionCardToPlay);
        eventEnqueuer.enqueue(playerEvent);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player player)) {
            return false;
        }

        if (this.playerId.id() == null && player.playerId.id() == null) {
            return false;
        }

        return Objects.equals(playerId.id(), player.playerId.id());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId.id());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("id=" + playerId.id())
                .toString();
    }
}
