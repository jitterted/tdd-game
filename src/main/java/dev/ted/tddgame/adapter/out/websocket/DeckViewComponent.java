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
        HtmlElement[] imgElement;
        if (actionCardDeckView.drawPile().isEmpty()) {
            imgElement = new HtmlElement[0];
        } else {
            imgElement = new HtmlElement[]{
                    HtmlElement.img("/action-card-back.png",
                                    "Action Card Draw Pile")};
        }
        return HtmlElement.swapInnerHtml(
                "action-card-draw-pile",
                imgElement
        );
    }
}
