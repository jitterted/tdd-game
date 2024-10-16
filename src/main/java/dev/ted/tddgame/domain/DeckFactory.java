package dev.ted.tddgame.domain;

import java.util.List;

public class DeckFactory {

//    public DeckFactory() {
//    }

    public Deck<ActionCard> createDeck(List<ActionCard> actionCards) {
        return Deck.createForTest(actionCards);
    }
}