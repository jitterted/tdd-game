package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class Player {
    private final PlayerId playerId;
    private final MemberId memberId;
    private final String playerName;
    private final List<ActionCard> actionCards = new ArrayList<>();
    private final EventEnqueuer eventEnqueuer;
    private final Workspace workspace;

    public Player(PlayerId playerId, MemberId memberId,
                  String playerName, EventEnqueuer eventEnqueuer) {
        this.playerId = playerId;
        this.memberId = memberId;
        this.playerName = playerName;
        this.eventEnqueuer = eventEnqueuer;
        workspace = new Workspace(this);
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
        return actionCards.stream();
    }

    public void apply(PlayerEvent event) {
        switch (event) {
            case PlayerDrewActionCard playerDrewActionCard ->
                    actionCards.add(playerDrewActionCard.actionCard());

            case PlayerDiscardedActionCard playerDiscardedActionCard -> {
                actionCards.remove(playerDiscardedActionCard.actionCard());
                // tell Workspace.discard() // does not need to know the specific card
            }
        }
    }

    public void drawToFullFrom(ActionCardDeck actionCardDeck) {
        drawCardFrom(actionCardDeck);
        drawCardFrom(actionCardDeck);
        drawCardFrom(actionCardDeck);
        drawCardFrom(actionCardDeck);
        drawCardFrom(actionCardDeck);
    }

    private void drawCardFrom(ActionCardDeck actionCardDeck) {
        // constraint: Player's Hand must have room for an additional card
        PlayerEvent event =
                new PlayerDrewActionCard(memberId, actionCardDeck.draw());
        eventEnqueuer.enqueue(event);
    }

    void discard(ActionCard actionCardToDiscard, ActionCardDeck actionCardDeck) {
        // check constraint: actionCardToDiscard MUST be in the Player's Hand
        PlayerEvent playerEvent =
                new PlayerDiscardedActionCard(memberId, actionCardToDiscard);
        eventEnqueuer.enqueue(playerEvent);
        actionCardDeck.acceptDiscard(actionCardToDiscard);
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
