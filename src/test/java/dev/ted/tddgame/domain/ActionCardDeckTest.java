package dev.ted.tddgame.domain;

import dev.ted.tddgame.adapter.in.web.GameScenarioBuilder;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ActionCardDeckTest {

    private static final MemberId IRRELEVANT_MEMBER_ID = new MemberId(42L);

    @Nested
    class newActionCardDeck {

        @Test
        void hasEmptyDrawPile() {
            List<ActionCard> actionCards = List.of(
                    ActionCard.PREDICT,
                    ActionCard.WRITE_CODE);
            Deck<ActionCard> deck = ActionCardDeck.create(actionCards, null, new Deck.RandomShuffler<>());

            assertThat(deck.isDrawPileEmpty())
                    .as("Draw Pile should be empty when Deck is created")
                    .isTrue();
        }

        @Test
        void drawnCardsAreShuffledFromDiscardPileWhenDrawPileIsEmpty() {
            MemberId memberId = new MemberId(123L);
            Game game = GameScenarioBuilder.create()
                                           .unshuffledActionCards()
                                           .memberJoinsAsPlayer(memberId)
                                           .reconstitutedGameFromStore();

            game.drawActionCard(memberId);

            new EventsAssertion(game.freshEvents())
                    .hasSize(2)
                    .hasEventMatching(new Condition<>(
                                              gameEvent -> gameEvent.getClass() ==
                                                           ActionCardDeckReplenished.class,
                                              "ActionCardDeckReplenished event not found")
                            , new Condition<>(gameEvent -> (
                                                                   (ActionCardDeckReplenished) gameEvent).cardsInDrawPile().size() == 63,
                                              "Must be 63 cards in the ActionCardDeckReplenished event")
                    )
                    .hasOccurrences(PlayerDrewActionCard.class, 1);
        }

        @Test
        void viewHasNonEmptyDrawAndDiscardPiles() {
            ActionCardDeck deck = ActionCardDeck.createForTest();

            deck.apply(new ActionCardDeckReplenished(List.of(
                    ActionCard.WRITE_CODE,
                    ActionCard.LESS_CODE,
                    ActionCard.PREDICT)));

            assertThat(deck.view().drawPile())
                    .containsExactly(
                            ActionCard.WRITE_CODE,
                            ActionCard.LESS_CODE,
                            ActionCard.PREDICT);
        }
    }

    @Nested
    class CommandGeneratesEvents {
        @Test
        void emptyDrawPileDrawOneCardGeneratesReplenishAndCardDrawnEvents() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.PREDICT);

            MemberId memberId = new MemberId(42L);
            List<GameEvent> gameEvents = deck.drawFor(memberId);

            assertThat(gameEvents)
                    .containsExactly(
                            new ActionCardDeckReplenished(List.of(ActionCard.PREDICT)),
                            new PlayerDrewActionCard(memberId, ActionCard.PREDICT));
        }

        @Test
        void drawSecondCardDrawsSecondCardInDeckAfterReplenishAndCardDrawn() {
            MemberId memberId = new MemberId(42L);
            ActionCardDeck deck = ActionCardDeck.createForTest();
            deck.apply(new ActionCardDeckReplenished(List.of(ActionCard.LESS_CODE, ActionCard.WRITE_CODE)));
            deck.apply(new PlayerDrewActionCard(memberId, ActionCard.LESS_CODE));

            List<GameEvent> gameEvents = deck.drawFor(memberId);

            assertThat(gameEvents)
                    .containsExactly(
                            new PlayerDrewActionCard(memberId, ActionCard.WRITE_CODE)
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
        void cardDrawnEventRemovesCardFromDeck() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.REFACTOR,
                                                               ActionCard.CODE_BLOAT);
            deck.apply(new ActionCardDeckReplenished(
                    List.of(ActionCard.REFACTOR,
                            ActionCard.CODE_BLOAT)));

            deck.apply(new PlayerDrewActionCard(IRRELEVANT_MEMBER_ID,
                                                ActionCard.REFACTOR));

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
            deck.apply(new ActionCardDeckReplenished(
                    List.of(ActionCard.REFACTOR,
                            ActionCard.CODE_BLOAT)));

            assertThatIllegalStateException()
                    .isThrownBy(() -> deck.apply(
                            new PlayerDrewActionCard(IRRELEVANT_MEMBER_ID,
                                                     ActionCard.PREDICT)))
                    .withMessage("Card drawn from DrawPile did not match card in event = PlayerDrewActionCard[memberId=MemberId[id=42], actionCard=PREDICT], card drawn = REFACTOR");
        }

        @Test
        void exceptionWhenDeckCardDrawnRemovesFromEmptyDrawPile() {
            ActionCardDeck deck = ActionCardDeck.createForTest(ActionCard.REFACTOR,
                                                               ActionCard.CODE_BLOAT);

            // draw pile is empty, because we haven't applied a DeckReplenished event

            assertThatIllegalStateException()
                    .isThrownBy(() -> deck.apply(
                            new PlayerDrewActionCard(IRRELEVANT_MEMBER_ID,
                                                     ActionCard.REFACTOR)))
                    .withMessage("DrawPile must not be empty when applying event: PlayerDrewActionCard[memberId=MemberId[id=42], actionCard=REFACTOR]");
        }
    }

}