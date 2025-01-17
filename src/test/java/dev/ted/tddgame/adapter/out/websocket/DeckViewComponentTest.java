package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.DeckView;
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
        DeckViewComponent deckViewComponent = new DeckViewComponent(deckView);

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
    void drawPileWithCardsAndEmptyDiscardPileHasImageOnlyInsideDrawPileSwap() {
        DeckView<ActionCard> deckView = new DeckView<>(List.of(ActionCard.WRITE_CODE),
                                                       Collections.emptyList());
        DeckViewComponent deckViewComponent = new DeckViewComponent(deckView);

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
    void drawPileEmptyAndDiscardPileWithCardHasImageOfCardInDiscardPileSwap() {
        DeckView<ActionCard> deckView = new DeckView<>(Collections.emptyList(),
                                                       List.of(ActionCard.PREDICT));
        DeckViewComponent deckViewComponent = new DeckViewComponent(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "action-card-draw-pile"
                        ),
                        swapInnerHtml(
                                "action-card-discard-pile",
                                HandViewComponent.imgElementFor(ActionCard.PREDICT)
                        )
                ));
    }

    @Test
    void drawPileEmptyAndDiscardPileWithTwoCardsHasImageOfLastCardInDiscardPileSwap() {
        DeckView<ActionCard> deckView = new DeckView<>(Collections.emptyList(),
                                                       List.of(ActionCard.WRITE_CODE,
                                                               ActionCard.REFACTOR,
                                                               ActionCard.LESS_CODE));
        DeckViewComponent deckViewComponent = new DeckViewComponent(deckView);

        assertThat(deckViewComponent.htmlForDiscardAndDrawPiles())
                .isEqualTo(forest(
                        swapInnerHtml(
                                "action-card-draw-pile"
                        ),
                        swapInnerHtml(
                                "action-card-discard-pile",
                                HandViewComponent.imgElementFor(ActionCard.LESS_CODE)
                        )
                ));
    }
}