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
            ActionCardDeck deck = ActionCardDeck.create(actionCards, null, new Deck.RandomShuffler<>());

            assertThat(deck.isDrawPileEmpty())
                    .as("Draw Pile should be empty when Deck is created")
                    .isTrue();
        }

        @Test
        void canDrawFullSetOfCardsAfterReplenishTriggeredByDraw()  {
            ActionCardDeck deck = ActionCardDeck.createForTest(
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
        void drawnCardsAreShuffledFromDiscardPile() {
            List<ActionCard> allStandardActionCards = createStandardActionCards();
            ActionCardDeck deck = ActionCardDeck.createForRandomTest(allStandardActionCards);

            List<ActionCard> drawnCards = new ArrayList<>();
            do {
                drawnCards.add(deck.draw());
            } while (!deck.isDrawPileEmpty());

            assertThat(drawnCards)
                    .doesNotContainSequence(allStandardActionCards);

        }

        @Test
        void viewHasNonEmptyDrawAndDiscardPiles() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.WRITE_CODE,
                                                               ActionCard.LESS_CODE,
                                                               ActionCard.PREDICT);

            deck.draw(); // force replenish by drawing the WRITE_CODE

            assertThat(deck.view().drawPile())
                    .containsExactly(ActionCard.LESS_CODE,
                                     ActionCard.PREDICT);
        }
    }

    @Nested
    class CommandGeneratesEvents {
        @Test
        void emptyDrawPileDrawOneCardGeneratesReplenishAndCardDrawnEvents() {
            List<DeckEvent> deckEventsReceiver = new ArrayList<>();
            ActionCardDeck deck = ActionCardDeck.createForTest(deckEventsReceiver,
                                                               ActionCard.PREDICT);

            deck.draw();

            assertThat(deckEventsReceiver)
                    .containsExactly(
                            new ActionCardDeckReplenished(List.of(ActionCard.PREDICT)),
                            new ActionCardDrawn(ActionCard.PREDICT));
        }

        @Test
        void drawPileWithTwoCardsDrawTwoCardsGeneratesReplenishAndTwoCardDrawnEvents() {
            List<DeckEvent> deckEventsReceiver = new ArrayList<>();
            ActionCardDeck deck = ActionCardDeck.createForTest(deckEventsReceiver,
                                                               ActionCard.LESS_CODE,
                                                               ActionCard.WRITE_CODE);
            deck.draw();
            deck.draw();

            assertThat(deckEventsReceiver)
                    .containsExactly(
                            new ActionCardDeckReplenished(List.of(ActionCard.LESS_CODE,
                                                                    ActionCard.WRITE_CODE)),
                            new ActionCardDrawn(ActionCard.LESS_CODE),
                            new ActionCardDrawn(ActionCard.WRITE_CODE)
                    );
        }

    }

    @Nested
    class EventsProjectState {

        @Test
        void deckReplenishedEventMovesCardsIntoDrawPile() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.LESS_CODE,
                                                               ActionCard.CANT_ASSERT);
            ActionCardDeckReplenished deckEvent =
                    new ActionCardDeckReplenished(List.of(
                            ActionCard.LESS_CODE,
                            ActionCard.CANT_ASSERT));

            deck.apply(deckEvent);

            assertThat(deck.view().drawPile())
                    .as("Draw Pile contents not as expected")
                    .containsExactly(ActionCard.LESS_CODE,
                                     ActionCard.CANT_ASSERT);
            assertThat(deck.view().discardPile())
                    .as("Expected Discard Pile to be Empty")
                    .isEmpty();
        }

        @Test
        void deckCardDrawnEventRemovesCardFromDeck() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.REFACTOR,
                                                               ActionCard.CODE_BLOAT);
            deck.apply(new ActionCardDeckReplenished(List.of(ActionCard.REFACTOR,
                                                               ActionCard.CODE_BLOAT)));

            deck.apply(new ActionCardDrawn(ActionCard.REFACTOR));

            assertThat(deck.view().drawPile())
                    .as("Draw Pile contents not as expected")
                    .containsExactly(ActionCard.CODE_BLOAT);
            assertThat(deck.view().discardPile())
                    .as("Expected Discard Pile to be Empty")
                    .isEmpty();
        }

        @Test
        void exceptionWhenDeckCardDrawnHasDifferentCardThanDrawnFromDrawPile() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.REFACTOR,
                                                               ActionCard.CODE_BLOAT);
            deck.apply(new ActionCardDeckReplenished(List.of(ActionCard.REFACTOR,
                                                               ActionCard.CODE_BLOAT)));

            assertThatIllegalStateException()
                    .isThrownBy(() -> deck.apply(
                            new ActionCardDrawn(ActionCard.PREDICT)))
                    .withMessage("Card drawn from DrawPile did not match card in event = ActionCardDrawn[card=PREDICT], card drawn = REFACTOR");
        }

        @Test
        void exceptionWhenDeckCardDrawnRemovesFromEmptyDrawPile() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.REFACTOR,
                                                               ActionCard.CODE_BLOAT);

            // draw pile is empty, because we haven't applied a ActionCardDeckReplenished event

            assertThatIllegalStateException()
                    .isThrownBy(() -> deck.apply(
                            new ActionCardDrawn(ActionCard.REFACTOR)))
                    .withMessage("DrawPile must not be empty when applying event: ActionCardDrawn[card=REFACTOR]");
        }
    }


    // HELPER METHODS

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

    private List<ActionCard> drawCards(ActionCardDeck deck, int numberOfCards) {
        List<ActionCard> drawnCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            drawnCards.add(deck.draw());
        }
        return drawnCards;
    }

}