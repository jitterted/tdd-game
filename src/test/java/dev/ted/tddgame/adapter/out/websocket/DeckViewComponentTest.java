package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.DeckView;
import dev.ted.tddgame.domain.TestResultsCard;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static dev.ted.tddgame.adapter.HtmlElement.forest;
import static dev.ted.tddgame.adapter.HtmlElement.swapInnerHtml;
import static org.assertj.core.api.Assertions.*;

class DeckViewComponentTest {

    @Test
    void bothPilesEmptyHasEmptySwapElements() {
        DeckView<ActionCard> deckView = new DeckView<>(Collections.emptyList(),
                                                       Collections.emptyList());
        DeckViewComponent<ActionCard> deckViewComponent = DeckViewComponent.forActionCardDeck(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "action-card-draw-pile"
                        ),
                        swapInnerHtml(
                                "action-card-discard-pile"
                        )
                ));
    }

    @Test
    void drawPileHasCardsAndDiscardPileIsEmptyThenImageOnlyInsideDrawPileSwap() {
        DeckView<ActionCard> deckView = new DeckView<>(List.of(ActionCard.WRITE_CODE),
                                                       Collections.emptyList());
        DeckViewComponent<ActionCard> deckViewComponent = DeckViewComponent.forActionCardDeck(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "action-card-draw-pile",
                                HtmlElement.img("/action-card-back.png",
                                                "Action Card Draw Pile")
                        ),
                        swapInnerHtml(
                                "action-card-discard-pile"
                        )
                ));
    }

    @Test
    void drawPileEmptyAndDiscardPileHasCardThenImageOfCardInDiscardPileSwap() {
        DeckView<ActionCard> deckView = new DeckView<>(Collections.emptyList(),
                                                       List.of(ActionCard.PREDICT));
        DeckViewComponent<ActionCard> deckViewComponent = DeckViewComponent.forActionCardDeck(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "action-card-draw-pile"
                        ),
                        swapInnerHtml(
                                "action-card-discard-pile",
                                CardViewComponent.of(ActionCard.PREDICT).html()
                        )
                ));
    }

    @Test
    void drawPileEmptyAndDiscardPileWithTwoCardsHasImageOfLastCardInDiscardPileSwap() {
        DeckView<ActionCard> deckView = new DeckView<>(Collections.emptyList(),
                                                       List.of(ActionCard.WRITE_CODE,
                                                               ActionCard.REFACTOR,
                                                               ActionCard.LESS_CODE));
        DeckViewComponent<ActionCard> deckViewComponent = DeckViewComponent.forActionCardDeck(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "action-card-draw-pile"
                        ),
                        swapInnerHtml(
                                "action-card-discard-pile",
                                CardViewComponent.of(ActionCard.LESS_CODE).html()
                        )
                ));
    }

    @Test
    void testResultsDrawPileHasCardsAndDiscardPileIsEmptyThenImageOnlyInsideDrawPileSwap() {
        DeckView<TestResultsCard> deckView = new DeckView<>(List.of(TestResultsCard.AS_PREDICTED),
                                                           Collections.emptyList());
        DeckViewComponent<TestResultsCard> deckViewComponent = DeckViewComponent.forTestResultsCardDeck(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "test-results-card-draw-pile",
                                HtmlElement.img("/test-results-card-back.png",
                                                "Test Results Card Draw Pile")
                        ),
                        swapInnerHtml(
                                "test-results-card-discard-pile"
                        )
                ));
    }

    @Test
    void testResultsDrawPileEmptyAndDiscardPileHasCardThenImageOfCardInDiscardPileSwap() {
        DeckView<TestResultsCard> deckView = new DeckView<>(Collections.emptyList(),
                                                           List.of(TestResultsCard.NEED_ONE_LESS_CODE));
        DeckViewComponent<TestResultsCard> deckViewComponent = DeckViewComponent.forTestResultsCardDeck(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "test-results-card-draw-pile"
                        ),
                        swapInnerHtml(
                                "test-results-card-discard-pile",
                                CardViewComponent.of(TestResultsCard.NEED_ONE_LESS_CODE).html()
                        )
                ));
    }
}
