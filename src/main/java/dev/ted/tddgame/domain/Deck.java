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
    private final EventEnqueuer eventEnqueuer;
    private final List<CARD> discardPile;

    protected Deck(List<CARD> cards,
                Shuffler<CARD> shuffler,
                EventEnqueuer eventEnqueuer) {
        this.discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
        this.eventEnqueuer = eventEnqueuer;
    }

    // -- FOR TESTS --
    protected Deck(List<CARD> cards, Shuffler<CARD> shuffler, List<DeckEvent> deckEventsReceiver) {
        this.discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
        this.eventEnqueuer = new DeckEventEnqueuer<>(this, deckEventsReceiver);
    }

    void acceptDiscard(CARD discardedCard) {
        eventEnqueuer.enqueue(createCardDiscardedEvent(discardedCard));
    }

    protected abstract DeckEvent createCardDiscardedEvent(CARD discardedCard);

    public CARD draw() {
        if (isDrawPileEmpty()) {
            replenishDrawPileFromDiscardPile();
            if (isDrawPileEmpty()) {
                throw new IllegalStateException("Draw Pile must not be empty after Replenish");
            }
        }
        CARD drawnCard = drawPile.peek();
        eventEnqueuer.enqueue(createCardDrawnEvent(drawnCard));
        return drawnCard;
    }

    protected abstract DeckEvent createCardDrawnEvent(CARD drawnCard);

    private void replenishDrawPileFromDiscardPile() {
        // TODO - PRECONDITION: discardPile must NOT be empty
        List<Card> shuffledDiscardedCards = (List<Card>) shuffler.shuffleCards(discardPile);
        eventEnqueuer.enqueue(createDeckReplenishedEvent(shuffledDiscardedCards));
    }

    protected abstract DeckEvent createDeckReplenishedEvent(List<Card> shuffledDiscardedCards);

    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    public DeckView<CARD> view() {
        return new DeckView<>(List.copyOf(drawPile),
                              List.copyOf(discardPile));
    }

    public void apply(DeckEvent deckEvent) {
        switch (deckEvent) {
            case DeckReplenished deckReplenished -> {
                drawPile.addAll((Collection<? extends CARD>) deckReplenished.cardsInDrawPile());
                discardPile.clear();
            }

            case CardDrawn cardDrawn -> {
                if (drawPile.isEmpty()) {
                    throw new IllegalStateException("DrawPile must not be empty when applying event: " + cardDrawn);
                }
                CARD removedCard = drawPile.remove();
                if (!cardDrawn.card()
                              .equals(removedCard)) {
                    throw new IllegalStateException("Card drawn from DrawPile did not match card in event = %s, card drawn = %s"
                                                            .formatted(cardDrawn, removedCard));
                }
            }

            case CardDiscarded cardDiscarded -> {
                discardPile.add((CARD) cardDiscarded.card());
            }

            default -> throw new IllegalStateException("Unexpected DeckEvent value: " + deckEvent);
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

class DeckEventEnqueuer<CARD extends Card> implements EventEnqueuer {
    private final Deck<CARD> deck;
    private final List<DeckEvent> deckEvents;

    public DeckEventEnqueuer(Deck<CARD> deck, List<DeckEvent> deckEvents) {
        this.deck = deck;
        this.deckEvents = deckEvents;
    }

    @Override
    public void enqueue(GameEvent gameEvent) {
        if (gameEvent instanceof DeckEvent deckEvent) {
            deck.apply(deckEvent);
            deckEvents.add(deckEvent);
        }
    }
}
