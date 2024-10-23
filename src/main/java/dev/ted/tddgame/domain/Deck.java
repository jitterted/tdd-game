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
    private List<CARD> discardPile;

    // TODO: for production usage, uses random shuffler
    // TODO: must never accept a list of cards that is empty
    public static <CARD> Deck<CARD> create(List<CARD> cards) {
        return new Deck<>(cards, new RandomShuffler<>());
    }

    public static Deck<ActionCard> createForTest(ActionCard... actionCards) {
        return createForTest(Arrays.asList(actionCards));
    }

    public static Deck<ActionCard> createForTest(List<ActionCard> actionCards) {
        return new Deck<>(new ArrayList<>(actionCards),
                          new IdentityShuffler<>());
    }

    private Deck(List<CARD> cards, Shuffler<CARD> shuffler) {
        discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
    }

    CARD draw(EventEnqueuer eventEnqueuer) {
        if (drawPile.isEmpty()) {
            replenishDrawPileFromDiscardPile(eventEnqueuer);
        }
        CARD drawnCard = drawPile.peek();
        eventEnqueuer.enqueue(new DeckCardDrawn<>(drawnCard));
        return drawnCard;
    }

    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    private void replenishDrawPileFromDiscardPile(EventEnqueuer eventEnqueuer) {
        discardPile = shuffler.shuffleCards(discardPile);
        eventEnqueuer.enqueue(new DeckReplenished<>(discardPile));
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

            case DeckCardDrawn<CARD> deckCardDrawn -> {
                CARD removedCard = drawPile.remove();
                if (!deckCardDrawn.card().equals(removedCard)) {
                    throw new IllegalStateException();
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
