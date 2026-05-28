package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class Player {
    static final int PLAYER_HAND_FULL_SIZE = 5;
    private final PlayerId playerId;
    private final MemberId memberId;
    private final String playerName;
    private final List<ActionCard> hand = new ArrayList<>();
    private final Workspace workspace;

    public Player(PlayerId playerId,
                  MemberId memberId,
                  String playerName,
                  Workspace workspace) {
        this.playerId = playerId;
        this.memberId = memberId;
        this.playerName = playerName;
        this.workspace = workspace;
    }

    public static Player createForTest(long id, String playerName) {
        PlayerId playerId = new PlayerId(id);
        return new Player(playerId,
                          new MemberId(42L),
                          playerName,
                          new Workspace(playerId));
    }

    public void apply(GameEvent event) {
        switch (event) {
            case PlayerDrewActionCard(_, ActionCard actionCard) ->
                    hand.add(actionCard);

            case PlayerDrewTechNeglectCard(_, ActionCard actionCard) ->
                    workspace.techNeglectCardPlayed(actionCard);

            case PlayerDrewTestResultsCard(_, TestResultsCard testResultsCard) ->
                    workspace.testResultsCardDrawn(testResultsCard);

            case PlayerDiscardedActionCard(_, ActionCard actionCard) -> {
                hand.remove(actionCard);
                workspace.cardDiscarded();
            }

            case PlayerPlayedActionCard(_, ActionCard actionCard) -> {
                hand.remove(actionCard);
                workspace.cardPlayed(actionCard);
            }

            default -> throw new IllegalStateException("Unexpected Event: " + event);
        }
    }

    //region Commands
    List<GameEvent> drawCardFrom(Deck<ActionCard> actionCardDeck) {
        ensureHandNotFull();

        return actionCardDeck.drawFor(memberId);
    }

    List<GameEvent> discard(ActionCard actionCardToDiscard, Deck<ActionCard> actionCardDeck) {
        // check constraint: actionCardToDiscard MUST be in the Player's Hand
        PlayerDiscardedActionCard playerEvent =
                new PlayerDiscardedActionCard(memberId, actionCardToDiscard);
        return List.of(playerEvent);
    }

    List<GameEvent> playCard(ActionCard actionCardToPlay) {
        // check constraint: actionCardToPlay MUST be in the Player's Hand
        // check constraint: must check with Workspace to decide if this is allowed
        PlayerPlayedActionCard playerEvent =
                new PlayerPlayedActionCard(memberId, actionCardToPlay);
        return List.of(playerEvent);
    }

    List<GameEvent> drawTestResultsCardFrom(Deck<TestResultsCard> testResultsCardDeck) {
        if (workspace.drawnTestResultsCard() != null) {
            throw new IllegalStateException();
        }
        // check constraint: PREDICT card MUST be in-play (in the workspace) in order for this draw() to be allowed

        return testResultsCardDeck.drawFor(memberId);
    }
    //endregion

    //region Queries
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

    private void ensureHandNotFull() {
        if (handIsFull()) {
            throw new HandAlreadyFull("Can't draw any more cards, the Hand is full with five cards");
        }
    }

    public boolean handIsFull() {
        return hand.size() == PLAYER_HAND_FULL_SIZE;
    }
    //endregion


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
