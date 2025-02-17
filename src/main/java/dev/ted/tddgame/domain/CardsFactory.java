package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardsFactory {

    // 63 cards in this standard action card deck
    public List<ActionCard> createAllActionCards() {
        List<ActionCard> allActionCards = new ArrayList<>();
        allActionCards.addAll(Collections.nCopies(18, ActionCard.WRITE_CODE));
        allActionCards.addAll(Collections.nCopies(18, ActionCard.LESS_CODE));
        allActionCards.addAll(Collections.nCopies(18, ActionCard.PREDICT));
        allActionCards.addAll(Collections.nCopies(2, ActionCard.CANT_ASSERT));
        allActionCards.addAll(Collections.nCopies(3, ActionCard.CODE_BLOAT));
        allActionCards.addAll(Collections.nCopies(4, ActionCard.REFACTOR));
        return allActionCards;
    }

    public List<TestResultsCard> createAllTestResultsCards() {
        List<TestResultsCard> allTestResultsCards = new ArrayList<>();

        allTestResultsCards.addAll(Collections.nCopies(4, TestResultsCard.AS_PREDICTED));
        allTestResultsCards.addAll(Collections.nCopies(6, TestResultsCard.NEED_TWO_LESS_CODE));
        allTestResultsCards.addAll(Collections.nCopies(8, TestResultsCard.NEED_ONE_LESS_CODE));

        return allTestResultsCards;
    }
}