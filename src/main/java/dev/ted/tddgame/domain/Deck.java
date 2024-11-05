package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Deck<CARD> {
    private final Shuffler<CARD> shuffler;
    private final Queue<CARD> drawPile = new LinkedList<>();
    private final EventEnqueuer eventEnqueuer;
    private List<CARD> discardPile;

    // TODO: for production usage, uses random shuffler
    // TODO: must never accept a list of cards that is empty
    public static <CARD> Deck<CARD> create(List<CARD> cards,
                                           EventEnqueuer eventEnqueuer) {
        return new Deck<>(cards, new RandomShuffler<>(), eventEnqueuer);
    }

    public static Deck<ActionCard> createForTest(ActionCard... actionCards) {
        return createForTest(Arrays.asList(actionCards));
    }

    public static Deck<ActionCard> createForTest(List<ActionCard> actionCards) {
        return new Deck<>(actionCards,
                          new IdentityShuffler<>(),
                          new ArrayList<>());
    }

    public static Deck<ActionCard> createForRandomTest(List<ActionCard> actionCards) {
        return new Deck<>(actionCards,
                          new RandomShuffler<>(),
                          new ArrayList<>());
    }

    public static Deck<ActionCard> createForTest(List<DeckEvent<ActionCard>> deckEventsReceiver,
                                                 ActionCard... actionCards) {
        return new Deck<>(Arrays.asList(actionCards),
                          new IdentityShuffler<>(),
                          deckEventsReceiver);
    }

    public static Deck<ActionCard> createForTest(EventEnqueuer eventEnqueuer,
                                                 List<ActionCard> actionCards) {
        return new Deck<>(actionCards,
                          new IdentityShuffler<>(),
                          eventEnqueuer);
    }

    // intended for Production only
    private Deck(List<CARD> cards,
                 Shuffler<CARD> shuffler,
                 EventEnqueuer eventEnqueuer) {
        discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
        this.eventEnqueuer = eventEnqueuer;
    }

    // intended for tests only
    private Deck(List<CARD> cards,
                 Shuffler<CARD> shuffler,
                 List<DeckEvent<CARD>> deckEventsReceiver) {
        discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
        this.eventEnqueuer = new DeckEventEnqueuer<>(this, deckEventsReceiver);
    }

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

    protected ActionCardDrawn createCardDrawnEvent(CARD drawnCard) {
        return new ActionCardDrawn((ActionCard) drawnCard);
    }

    private void replenishDrawPileFromDiscardPile() {
        discardPile = shuffler.shuffleCards(discardPile);
        eventEnqueuer.enqueue(new DeckReplenished<>(discardPile));
    }

    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    public DeckView<CARD> view() {
        return new DeckView<>(List.copyOf(drawPile),
                              List.copyOf(discardPile));
    }

    public void apply(DeckEvent<CARD> deckEvent) {
        switch (deckEvent) {
            case DeckReplenished<CARD> deckReplenished -> {
                drawPile.addAll(deckReplenished.cardsInDrawPile());
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

    private static class IdentityShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            return new ArrayList<>(discardPile);
        }
    }

    private static class RandomShuffler<CARD> implements Shuffler<CARD> {
        @Override
        public List<CARD> shuffleCards(List<CARD> discardPile) {
            List<CARD> toBeShuffled = new ArrayList<>(discardPile);
            Collections.shuffle(toBeShuffled);
            return toBeShuffled;
        }
    }

    private interface Shuffler<CARD> {
        List<CARD> shuffleCards(List<CARD> discardPile);
    }
}

class DeckEventEnqueuer<CARD> implements EventEnqueuer {
    private final Deck<CARD> deck;
    private final List<DeckEvent<CARD>> deckEvents;

    public DeckEventEnqueuer(Deck<CARD> deck, List<DeckEvent<CARD>> deckEvents) {
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
