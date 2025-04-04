package dev.ted.tddgame.domain;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@NullMarked
public class Workspace {
    private final WorkspaceId id;
    private HexTile currentHexTile;
    private final List<ActionCard> cardsInPlay = new ArrayList<>();
    private final List<ActionCard> techNeglectCards = new ArrayList<>();
    private @Nullable TestResultsCard drawnTestResultsCard = null;

    /**
     * Assign the Workspace ID from the Player's ID
     * (This is temporary: it should have its own ID)
     *
     * @param playerId the "owner" of this workspace
     */
    public Workspace(PlayerId playerId) {
        this(playerId, HexTile.WHAT_SHOULD_IT_DO);
    }

    private Workspace(PlayerId playerId, HexTile currentHexTile) {
        id = new WorkspaceId(playerId.id());
        this.currentHexTile = currentHexTile;
    }

    public static Workspace createForTest(HexTile currentHexTile) {
        return new Workspace(new PlayerId(42L), currentHexTile);
    }

    public static Workspace createForTest() {
        return new Workspace(new PlayerId(42L));
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

    public @Nullable TestResultsCard drawnTestResultsCard() {
        return drawnTestResultsCard;
    }

    /**
     * Handler for a Test Results Card having been drawn
     */
    public void testResultsCardDrawn(TestResultsCard testResultsCard) {
        if (drawnTestResultsCard != null) {
            throw new IllegalStateException("Not allowed to draw another Test Results card if the current one in the workspace hasn't been processed (and discarded).");
        }

        drawnTestResultsCard = testResultsCard;
    }

    // COMMAND
    //    if "as expected", clean up workspace in-play (via discard)
    //    and discard TestResults Card (these might need to be handled outside the workspace)
    public void processTestResultsCard() {
        drawnTestResultsCard = null;
    }

    /**
     * Handler for Tech Neglect ActionCard that has been "played"
     */
    public void techNeglectCardPlayed(ActionCard actionCard) {
        techNeglectCards.add(actionCard);
    }
}
