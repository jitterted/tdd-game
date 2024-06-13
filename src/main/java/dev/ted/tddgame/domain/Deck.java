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

    // for production usage, uses random shuffler
    public static <CARD> Deck<CARD> create(List<CARD> cards) {
        return new Deck<>(cards, new RandomShuffler<>());
    }

    private Deck(List<CARD> cards, Shuffler<CARD> shuffler) {
        discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
    }

    public static Deck<ActionCard> createForTest(ActionCard... actionCards) {
        return new Deck<>(Collections.emptyList(),
                          _ -> new ArrayList<>(Arrays.asList(actionCards)));
    }

    public CARD draw() {
        if (drawPile.isEmpty()) {
            replenishDrawPileFromDiscardPile();
        }
        return drawPile.remove();
    }

    public boolean isDrawPileEmpty() {
        return drawPile.isEmpty();
    }

    private void replenishDrawPileFromDiscardPile() {
        discardPile = shuffler.shuffleCards(discardPile);
        drawPile.addAll(discardPile);
        discardPile.clear();
    }

    public DeckView<CARD> view() {
        return new DeckView<>(Collections.emptyList(),
                              List.copyOf(discardPile));
    }

    // -- EMBEDDED STUB for Nullable Shuffler --

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
