package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Deck<CARD> {
    protected Shuffler<CARD> shuffler; // TODO: should be final
    private final Queue<CARD> drawPile = new LinkedList<>();
    protected EventEnqueuer eventEnqueuer; // TODO: should be final
    protected List<CARD> discardPile; // TODO: should be final

    public CARD draw() {
        if (drawPile.isEmpty()) {
            replenishDrawPileFromDiscardPile();
        }
        CARD drawnCard = drawPile.peek();
        //      1. Check concrete type of CARD, e.g. if drawnCard.class == ActionCard, then generate ActionCardDrawn
        //      2. Ask CARD for its event representing card drawn, e.g., drawnCard.drawEvent()
        // [✅] 3. push down the event creation to a concrete subclass
        eventEnqueuer.enqueue(createCardDrawnEvent(drawnCard));
        return drawnCard;
    }

    protected abstract ActionCardDrawn createCardDrawnEvent(CARD drawnCard);

    private void replenishDrawPileFromDiscardPile() {
        discardPile = shuffler.shuffleCards(discardPile);
        eventEnqueuer.enqueue(new ActionCardDeckReplenished((List<ActionCard>) discardPile));
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
            case ActionCardDeckReplenished actionCardDeckReplenished -> {
                drawPile.addAll((Collection<? extends CARD>) actionCardDeckReplenished.cardsInDrawPile());
                discardPile.clear();
            }

            case ActionCardDrawn actionCardDrawn -> {
                if (drawPile.isEmpty()) {
                    throw new IllegalStateException("DrawPile must not be empty when applying event: " + actionCardDrawn);
                }
                CARD removedCard = drawPile.remove();
                if (!actionCardDrawn.card()
                                    .equals(removedCard)) {
                    throw new IllegalStateException("Card drawn from DrawPile did not match card in event = %s, card drawn = %s"
                                                            .formatted(actionCardDrawn, removedCard));
                }
            }
        }
    }

    // -- EMBEDDED STUB for Nullable Shuffler --

    protected static class IdentityShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            return new ArrayList<>(discardPile);
        }
    }

    protected static class RandomShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            List<CARD> toBeShuffled = new ArrayList<>(discardPile);
            Collections.shuffle(toBeShuffled);
            return toBeShuffled;
        }
    }

    protected interface Shuffler<CARD> {
        List<CARD> shuffleCards(List<CARD> discardPile);
    }
}

class DeckEventEnqueuer<CARD> implements EventEnqueuer {
    private final Deck<CARD> deck;
    private final List<DeckEvent> deckEvents;

    public DeckEventEnqueuer(Deck<CARD> deck, List<DeckEvent> deckEvents) {
        this.deck = deck;
        this.deckEvents = deckEvents;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void enqueue(GameEvent gameEvent) {
        if (gameEvent instanceof DeckEvent deckEvent) {
            deck.apply(deckEvent);
            deckEvents.add(deckEvent);
        }
    }
}
