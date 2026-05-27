package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Deck<CARD extends Card> {
    private final Shuffler<CARD> shuffler;
    private final Queue<CARD> drawPile = new LinkedList<>();
    private final List<CARD> discardPile;

    protected Deck(List<CARD> cards,
                Shuffler<CARD> shuffler,
                EventEnqueuer eventEnqueuer) {
        this.discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
    }

    // -- FOR TESTS --
    protected Deck(List<CARD> cards, Shuffler<CARD> shuffler, List<DeckEvent> deckEventsReceiver) {
        this.discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
    }

    public List<GameEvent> drawFor(MemberId memberId) {
        List<GameEvent> gameEvents = new ArrayList<>();
        CARD drawnCard;
        if (isDrawPileEmpty()) {
            // TODO - PRECONDITION: discardPile must NOT be empty
            List<Card> shuffledDiscardedCards = (List<Card>) shuffler.shuffleCards(discardPile);
            GameEvent deckReplenishedEvent = createDeckReplenishedEvent(shuffledDiscardedCards);
            gameEvents.add(deckReplenishedEvent);
            // when we do a drawPile.addAll(replenishedCards), queue.peek() returns the first item in the list
            drawnCard = (CARD) shuffledDiscardedCards.getFirst();
        } else {
            drawnCard = drawPile.peek();
        }
        gameEvents.add(createPlayerDrewCard(memberId, drawnCard));
        return gameEvents;
    }

    protected abstract GameEvent createPlayerDrewCard(MemberId memberId, CARD drawnCard);

    protected abstract GameEvent createDeckReplenishedEvent(List<Card> shuffledDiscardedCards);

    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    public DeckView<CARD> view() {
        return new DeckView<>(List.copyOf(drawPile),
                              List.copyOf(discardPile));
    }

    public void apply(GameEvent gameEvent) {
        switch (gameEvent) {
            case DeckReplenished deckReplenished -> {
                drawPile.addAll((Collection<? extends CARD>) deckReplenished.cardsInDrawPile());
                discardPile.clear();
            }

            case PlayerDrewTechNeglectCard playerDrewTechNeglectCard -> {
                handleDrawnCard(playerDrewTechNeglectCard,
                                playerDrewTechNeglectCard.techNeglectActionCard());
            }

            case PlayerDrewActionCard playerDrewActionCard -> {
                handleDrawnCard(playerDrewActionCard,
                                playerDrewActionCard.actionCard());
            }

            case PlayerDrewTestResultsCard playerDrewTestResultsCard -> {
                handleDrawnCard(playerDrewTestResultsCard,
                                playerDrewTestResultsCard.testResultsCard());
            }

            case PlayerDiscardedActionCard playerDiscardedActionCard -> {
                discardPile.add((CARD) playerDiscardedActionCard.actionCard());
            }

            default -> throw new IllegalStateException("Unexpected DeckEvent value: " + gameEvent);
        }
    }

    private void handleDrawnCard(GameEvent gameEvent, Card drawnCard) {
        if (drawPile.isEmpty()) {
            throw new IllegalStateException("DrawPile must not be empty when applying event: " + gameEvent);
        }
        CARD removedCard = drawPile.remove();
        if (!drawnCard.equals(removedCard)) {
            throw new IllegalStateException("Card drawn from DrawPile did not match card in event = %s, card drawn = %s"
                                                    .formatted(gameEvent, removedCard));
        }
    }

    // -- EMBEDDED STUB for Nullable Shuffler --

    public static final class IdentityShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            return new ArrayList<>(discardPile);
        }
    }

    public static final class RandomShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            List<CARD> toBeShuffled = new ArrayList<>(discardPile);
            Collections.shuffle(toBeShuffled);
            return toBeShuffled;
        }
    }

    public sealed interface Shuffler<CARD>
            permits IdentityShuffler, RandomShuffler {
        List<CARD> shuffleCards(List<CARD> discardPile);
    }
}
