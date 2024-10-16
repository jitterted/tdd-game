package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DeckTest {

    @Nested
    class newActionCardDeck {
        @Test
        void hasEmptyDrawPile() {
            List<ActionCard> actionCards = List.of(
                    ActionCard.PREDICT,
                    ActionCard.WRITE_CODE);
            Deck<ActionCard> deck = Deck.create(actionCards);

            assertThat(deck.isDrawPileEmpty())
                    .as("Draw Pile should be empty when Deck is created")
                    .isTrue();
        }

        @Test
        void canDrawFullSetOfCardsAfterReplenishTriggeredByDraw() throws Exception {
            Deck<ActionCard> deck = Deck.createForTest(
                    ActionCard.PREDICT,
                    ActionCard.WRITE_CODE,
                    ActionCard.REFACTOR,
                    ActionCard.LESS_CODE
            );

            List<ActionCard> drawnCards = drawCards(deck, 4);

            assertThat(drawnCards)
                    .containsExactly(ActionCard.PREDICT,
                                     ActionCard.WRITE_CODE,
                                     ActionCard.REFACTOR,
                                     ActionCard.LESS_CODE);

            assertThat(deck.isDrawPileEmpty())
                    .isTrue();
        }

        @Test
        void drawnCardsAreFromReplenishedShuffledDiscardPile() {
            List<ActionCard> allStandardActionCards = createStandardActionCards();
            Deck<ActionCard> deck = Deck.create(allStandardActionCards);

            List<ActionCard> drawnCards = new ArrayList<>();
            do {
                drawnCards.add(deck.draw());
            } while (!deck.isDrawPileEmpty());

            assertThat(drawnCards)
                    .doesNotContainSequence(allStandardActionCards);

        }

        @Test
        void viewHasNonEmptyDrawAndDiscardPiles() throws Exception {
            Deck<ActionCard> deck = Deck.createForTest(ActionCard.WRITE_CODE,
                                                       ActionCard.LESS_CODE,
                                                       ActionCard.PREDICT);

            deck.draw(); // force replenish by drawing the WRITE_CODE

            assertThat(deck.view().drawPile())
                    .containsExactly(ActionCard.LESS_CODE,
                                     ActionCard.PREDICT);
        }
    }

    private List<ActionCard> createStandardActionCards() {
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


    private List<ActionCard> drawCards(Deck<ActionCard> deck, int numberOfCards) {
        List<ActionCard> drawnCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            drawnCards.add(deck.draw());
        }
        return drawnCards;
    }

}