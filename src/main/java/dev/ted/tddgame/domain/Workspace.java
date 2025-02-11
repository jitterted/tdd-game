package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Workspace {
    private final WorkspaceId id;
    private HexTile currentHexTile;
    private final List<ActionCard> cardsInPlay = new ArrayList<>();
    private final List<ActionCard> techNeglectCards = new ArrayList<>();

    /**
     * Assign the Workspace ID from the Player's ID
     * (This is temporary: it should have its own ID)
     * @param playerId the "owner" of this workspace
     */
    public Workspace(PlayerId playerId) {
        id = new WorkspaceId(playerId.id());
        currentHexTile = HexTile.WHAT_SHOULD_IT_DO;
    }

    public WorkspaceId id() {
        return id;
    }

    public HexTile currentHexTile() {
        return currentHexTile;
    }

    /**
     * Handler for an ActionCard that was ALREADY discarded from the Player's hand.
     * And this is called during event application (vs. command invocation).
     * This is not the "decider", which can reject a discard, for that,
     * we have a query to ask if we can do this.
     * <br/>
     * This is not the same as "clean up" cards, which happens on a successful PREDICT
     */
    public void cardDiscarded() {
        currentHexTile = currentHexTile.cardDiscarded();
    }

    /**
     * Handler for a card that has already been played (this is not a COMMAND)
     */
    public void cardPlayed(ActionCard actionCard) {
        currentHexTile = currentHexTile.cardPlayed(actionCard);
        cardsInPlay.add(actionCard);
    }

    public Stream<ActionCard> cardsInPlay() {
        return cardsInPlay.stream();
    }

    public Stream<ActionCard> techNeglectCards() {
        return techNeglectCards.stream();
    }

    /**
     * Handler for Tech Neglect ActionCard that has been "played"
     */
    public void techNeglectCardPlayed(ActionCard actionCard) {
        techNeglectCards.add(actionCard);
    }
}
