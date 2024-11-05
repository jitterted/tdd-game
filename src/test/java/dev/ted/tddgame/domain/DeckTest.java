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
            Deck<ActionCard> deck = Deck.create(actionCards, null);

            assertThat(deck.isDrawPileEmpty())
                    .as("Draw Pile should be empty when Deck is created")
                    .isTrue();
        }

        @Test
        void canDrawFullSetOfCardsAfterReplenishTriggeredByDraw()  {
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
        void drawnCardsAreShuffledFromDiscardPile() {
            List<ActionCard> allStandardActionCards = createStandardActionCards();
            Deck<ActionCard> deck = Deck.createForRandomTest(allStandardActionCards);

            List<ActionCard> drawnCards = new ArrayList<>();
            do {
                drawnCards.add(deck.draw());
            } while (!deck.isDrawPileEmpty());

            assertThat(drawnCards)
                    .doesNotContainSequence(allStandardActionCards);

        }

        @Test
        void viewHasNonEmptyDrawAndDiscardPiles() {
            Deck<ActionCard> deck = Deck.createForTest(ActionCard.WRITE_CODE,
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
            List<DeckEvent<ActionCard>> deckEventsReceiver = new ArrayList<>();
            Deck<ActionCard> deck = Deck.createForTest(deckEventsReceiver,
                                                       ActionCard.PREDICT);

            deck.draw();

            assertThat(deckEventsReceiver)
                    .containsExactly(
                            new DeckReplenished<>(List.of(ActionCard.PREDICT)),
                            new ActionCardDrawn(ActionCard.PREDICT));
        }

        @Test
        void drawPileWithTwoCardsDrawTwoCardsGeneratesReplenishAndTwoCardDrawnEvents() {
            List<DeckEvent<ActionCard>> deckEventsReceiver = new ArrayList<>();
            Deck<ActionCard> deck = Deck.createForTest(deckEventsReceiver,
                                                       ActionCard.LESS_CODE,
                                                       ActionCard.WRITE_CODE);
            deck.draw();
            deck.draw();

            assertThat(deckEventsReceiver)
                    .containsExactly(
                            new DeckReplenished<>(List.of(ActionCard.LESS_CODE,
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
            Deck<ActionCard> deck = Deck.createForTest(ActionCard.LESS_CODE,
                                                       ActionCard.CANT_ASSERT);
            DeckReplenished<ActionCard> deckEvent =
                    new DeckReplenished<>(List.of(
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
            Deck<ActionCard> deck = Deck.createForTest(ActionCard.REFACTOR,
                                                       ActionCard.CODE_BLOAT);
            deck.apply(new DeckReplenished<>(List.of(ActionCard.REFACTOR,
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
            Deck<ActionCard> deck = Deck.createForTest(ActionCard.REFACTOR,
                                                       ActionCard.CODE_BLOAT);
            deck.apply(new DeckReplenished<>(List.of(ActionCard.REFACTOR,
                                                     ActionCard.CODE_BLOAT)));

            assertThatIllegalStateException()
                    .isThrownBy(() -> deck.apply(
                            new ActionCardDrawn(ActionCard.PREDICT)))
                    .withMessage("Card drawn from DrawPile did not match card in event = ActionCardDrawn[card=PREDICT], card drawn = REFACTOR");
        }

        @Test
        void exceptionWhenDeckCardDrawnRemovesFromEmptyDrawPile() {
            Deck<ActionCard> deck = Deck.createForTest(ActionCard.REFACTOR,
                                                       ActionCard.CODE_BLOAT);

            // draw pile is empty, because we haven't applied a DeckReplenished event

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

    private List<ActionCard> drawCards(Deck<ActionCard> deck, int numberOfCards) {
        List<ActionCard> drawnCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            drawnCards.add(deck.draw());
        }
        return drawnCards;
    }

}