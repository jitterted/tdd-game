package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.DeckView;

public class DeckViewComponent {
    private final DeckView<ActionCard> actionCardDeckView;

    public DeckViewComponent(DeckView<ActionCard> actionCardDeckView) {
        this.actionCardDeckView = actionCardDeckView;
    }

    HtmlElement htmlForDiscardAndDrawPiles() {
        return HtmlElement.forest(drawPile(),
                                  HtmlElement.swapInnerHtml(
                                          "action-card-discard-pile"
                                  ));
    }

    private HtmlElement drawPile() {
        if (actionCardDeckView.drawPile().isEmpty()) {
            return HtmlElement.swapInnerHtml(
                    "action-card-draw-pile");
        }

        return HtmlElement.swapInnerHtml(
                "action-card-draw-pile",
                HtmlElement.img("/action-card-back.png",
                                "Action Card Draw Pile")
        );
    }
}
