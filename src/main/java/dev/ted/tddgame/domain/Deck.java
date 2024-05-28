package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Deck<CARD> {
    private final Queue<CARD> drawPile = new LinkedList<>();
    private final List<CARD> discardPile = new ArrayList<>();

    public Deck(List<CARD> cards) {
        discardPile.addAll(cards);
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
        drawPile.addAll(discardPile);
        discardPile.clear();
    }
}
