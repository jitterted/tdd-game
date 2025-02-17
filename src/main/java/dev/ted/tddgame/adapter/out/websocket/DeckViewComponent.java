package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Card;
import dev.ted.tddgame.domain.DeckView;
import dev.ted.tddgame.domain.TestResultsCard;

import java.util.List;
import java.util.function.Function;

public class DeckViewComponent<C extends Card> {
    private final DeckView<C> deckView;
    private final String drawPileTargetId;
    private final String discardPileTargetId;
    private final HtmlElement backOfCardImgElement;
    private final Function<C, HtmlElement> cardToHtmlTransformer;

    private DeckViewComponent(DeckView<C> deckView, String drawPileTargetId, String discardPileTargetId, HtmlElement backOfCardImgElement, Function<C, HtmlElement> cardToHtmlTransformer) {
        this.deckView = deckView;
        this.drawPileTargetId = drawPileTargetId;
        this.discardPileTargetId = discardPileTargetId;
        this.backOfCardImgElement = backOfCardImgElement;
        this.cardToHtmlTransformer = cardToHtmlTransformer;
    }

    public static DeckViewComponent<ActionCard> forActionCardDeck(DeckView<ActionCard> actionCardDeckView) {
        return new DeckViewComponent<>(actionCardDeckView,
                                       "action-card-draw-pile",
                                       "action-card-discard-pile",
                                       HtmlElement.img("/action-card-back.png", "Action Card Draw Pile"),
                                       card -> HtmlElement.swapInnerHtml("action-card-discard-pile", CardViewComponent.of(card).html())
        );
    }

    public static DeckViewComponent<TestResultsCard> forTestResultsCardDeck(DeckView<TestResultsCard> testResultsCardDeckView) {
        return new DeckViewComponent<>(testResultsCardDeckView,
                                       "test-results-card-draw-pile",
                                       "test-results-card-discard-pile",
                                       HtmlElement.img("/test-results-card-back.png", "Test Results Card Draw Pile"),
                                       card -> HtmlElement.swapInnerHtml("test-results-card-discard-pile", CardViewComponent.of(card).html())
        );
    }

    HtmlElement htmlForDiscardAndDrawPiles() {
        return HtmlElement.forest(
                swapForPile(deckView.drawPile(),
                            drawPileTargetId,
                            _ -> HtmlElement.swapInnerHtml(drawPileTargetId, backOfCardImgElement)),
                swapForPile(deckView.discardPile(),
                            discardPileTargetId,
                            cardToHtmlTransformer)
        );
    }

    private HtmlElement swapForPile(List<C> pile, String targetId, Function<C, HtmlElement> cardToHtmlMapper) {
        if (pile.isEmpty()) {
            return HtmlElement.swapInnerHtml(targetId);
        }

        return cardToHtmlMapper.apply(pile.getLast());
    }
}
