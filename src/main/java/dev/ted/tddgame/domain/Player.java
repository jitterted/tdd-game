package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Player {
    private final MemberId memberId;
    private final String playerName;
    private final PlayerId playerId;
    private final ArrayList<ActionCard> actionCards = new ArrayList<>();

    public Player(PlayerId playerId, MemberId memberId, String playerName) {
        this.playerId = playerId;
        this.memberId = memberId;
        this.playerName = playerName;
    }

    public PlayerId id() {
        return playerId;
    }

    public MemberId memberId() {
        return memberId;
    }

    public String playerName() {
        return playerName;
    }

    public Stream<ActionCard> hand() {
        return actionCards.stream();
    }

    @Deprecated // will be replaced by event-triggered apply
    public void addCardToHand(ActionCard actionCard) {
        actionCards.add(actionCard);
    }

    public void drawToFullFrom(Deck<ActionCard> actionCardDeck,
                               Consumer<GameEvent> eventConsumer) {
        PlayerDrewActionCard event =
                new PlayerDrewActionCard(memberId, actionCardDeck.draw());
        eventConsumer.accept(event);
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
