package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Card;
import dev.ted.tddgame.domain.DeckView;

import java.util.List;
import java.util.function.Function;

public class DeckViewComponent {
    private final DeckView<? extends Card> actionCardDeckView;
    private final String drawPileTargetId;
    private final String discardPileTargetId;
    private final HtmlElement backOfCardImgElement;

    private DeckViewComponent(DeckView<? extends Card> actionCardDeckView, String drawPileTargetId, String discardPileTargetId, HtmlElement backOfCardImgElement) {
        this.actionCardDeckView = actionCardDeckView;
        this.drawPileTargetId = drawPileTargetId;
        this.discardPileTargetId = discardPileTargetId;
        this.backOfCardImgElement = backOfCardImgElement;
    }

    public static DeckViewComponent forActionCardDeck(DeckView<ActionCard> actionCardDeckView) {
        return new DeckViewComponent(actionCardDeckView,
                                     "action-card-draw-pile",
                                     "action-card-discard-pile",
                                     HtmlElement.img("/action-card-back.png", "Action Card Draw Pile")
        );
    }

    HtmlElement htmlForDiscardAndDrawPiles() {
        return HtmlElement.forest(
                swapForPile(actionCardDeckView.drawPile(),
                            drawPileTargetId,
                            _ -> HtmlElement.swapInnerHtml(drawPileTargetId, backOfCardImgElement)),
                swapForPile(actionCardDeckView.discardPile(),
                            discardPileTargetId,
                            actionCard -> HtmlElement.swapInnerHtml(discardPileTargetId, CardViewComponent.imgElementFor((ActionCard) actionCard)))
        );
    }

    private HtmlElement swapForPile(List<? extends Card> pile, String targetId, Function<Card, HtmlElement> cardToHtmlMapper) {
        if (pile.isEmpty()) {
            return HtmlElement.swapInnerHtml(targetId);
        }

        return cardToHtmlMapper.apply(pile.getLast());
    }
}
