package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Deck<CARD extends Card> {
    protected Shuffler<CARD> shuffler; // TODO: should be final
    private final Queue<CARD> drawPile = new LinkedList<>();
    protected EventEnqueuer eventEnqueuer; // TODO: should be final
    protected List<CARD> discardPile; // TODO: should be final

    public CARD draw() {
        if (drawPile.isEmpty()) {
            replenishDrawPileFromDiscardPile();
        }
        CARD drawnCard = drawPile.peek();
        //      1. Check concrete type of CARD, e.g. if drawnCard.class == ActionCard, then generate CardDrawn
        //      2. Ask CARD for its event representing card drawn, e.g., drawnCard.drawEvent()
        // [✅] 3. push down the event creation to a concrete subclass
        eventEnqueuer.enqueue(createCardDrawnEvent(drawnCard));
        return drawnCard;
    }

    protected abstract CardDrawn createCardDrawnEvent(CARD drawnCard);

    private void replenishDrawPileFromDiscardPile() {
        discardPile = shuffler.shuffleCards(discardPile);
        eventEnqueuer.enqueue(new DeckReplenished((List<Card>) discardPile));
    }

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
        }
    }

    void acceptDiscard(CARD discardedCard) {
        eventEnqueuer.enqueue(createCardDiscardedEvent(discardedCard));
    }

    protected abstract CardDiscarded createCardDiscardedEvent(CARD discardedCard);

    // -- EMBEDDED STUB for Nullable Shuffler --

    protected static class IdentityShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            return new ArrayList<>(discardPile);
        }
    }

    public static class RandomShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            List<CARD> toBeShuffled = new ArrayList<>(discardPile);
            Collections.shuffle(toBeShuffled);
            return toBeShuffled;
        }
    }

    public interface Shuffler<CARD> {
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
