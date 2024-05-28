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
            Deck<ActionCard> deck = new Deck<>(actionCards);

            assertThat(deck.isDrawPileEmpty())
                    .as("Draw Pile should be empty when Deck is created")
                    .isTrue();
        }

        @Test
        void canDrawFullSetOfCardsAfterShuffleTriggeredByDraw() throws Exception {
            List<ActionCard> actionCards = List.of(
                    ActionCard.PREDICT,
                    ActionCard.WRITE_CODE,
                    ActionCard.REFACTOR,
                    ActionCard.LESS_CODE);
            Deck<ActionCard> deck = new Deck<>(actionCards);

            List<ActionCard> drawnCards = drawCards(deck, 4);

            assertThat(drawnCards)
                    .containsExactly(ActionCard.PREDICT,
                                     ActionCard.WRITE_CODE,
                                     ActionCard.REFACTOR,
                                     ActionCard.LESS_CODE);

            assertThat(deck.isDrawPileEmpty())
                    .isTrue();
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