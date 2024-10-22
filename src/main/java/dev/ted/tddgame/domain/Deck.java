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

    // events that Deck generates/applies
    // * DeckCreated(cards to be put in discard pile)
    // * DeckReplenished(cards to be put in draw pile)

    // for production usage, uses random shuffler
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

    public CARD draw() {
        if (drawPile.isEmpty()) {
            // generate event to replenish from discard
            // enqueue it: which also APPLIES it (to change state)
            replenishDrawPileFromDiscardPile();
        }
        // if needed, the replenish already changed the state of drawPile
        return drawPile.remove();
    }

    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    private void replenishDrawPileFromDiscardPile() {
        discardPile = shuffler.shuffleCards(discardPile);
        // generate event: replenishTo(shuffle of discardPile)
        drawPile.addAll(discardPile); // happens in apply?
        discardPile.clear(); // happens in apply?
        // apply the event
    }

    public DeckView<CARD> view() {
        return new DeckView<>(List.copyOf(drawPile),
                              List.copyOf(discardPile));
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
