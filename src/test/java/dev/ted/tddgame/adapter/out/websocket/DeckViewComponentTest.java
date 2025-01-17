package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.DeckView;
import org.junit.jupiter.api.Test;

import java.util.Collections;

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
}