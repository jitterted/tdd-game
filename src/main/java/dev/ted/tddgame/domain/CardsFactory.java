package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.List;

public class CardsFactory {

    // 63 cards in this standard action card deck
    public List<ActionCard> createStandardActionCards() {
        List<ActionCard> allActionCards = new ArrayList<>();
        addActionCards(18, ActionCard.WRITE_CODE, allActionCards);
        addActionCards(18, ActionCard.LESS_CODE, allActionCards);
        addActionCards(18, ActionCard.PREDICT, allActionCards);
        addActionCards(2, ActionCard.CANT_ASSERT, allActionCards);
        addActionCards(3, ActionCard.CODE_BLOAT, allActionCards);
        addActionCards(4, ActionCard.REFACTOR, allActionCards);
        return allActionCards;
    }

    private void addActionCards(int count, ActionCard actionCard, List<ActionCard> allActionCards) {
        for (int i = 0; i < count; i++) {
            allActionCards.add(actionCard);
        }
    }
}