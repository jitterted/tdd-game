package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.DeckView;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class DeckViewComponent {
    private final DeckView<ActionCard> actionCardDeckView;

    public DeckViewComponent(DeckView<ActionCard> actionCardDeckView) {
        this.actionCardDeckView = actionCardDeckView;
    }

    HtmlElement htmlForDiscardAndDrawPiles() {
        return HtmlElement.forest(
                swapForPile(actionCardDeckView.drawPile(),
                            "action-card-draw-pile",
                            _ -> HtmlElement.swapInnerHtml("action-card-draw-pile", HtmlElement.img("/action-card-back.png", "Action Card Draw Pile"))),
                swapForPile(actionCardDeckView.discardPile(),
                            "action-card-discard-pile",
                            actionCard -> HtmlElement.swapInnerHtml("action-card-discard-pile", HandViewComponent.imgElementFor(actionCard)))
        );
    }

    private HtmlElement swapForPile(List<ActionCard> pile, String targetId, Function<@NotNull ActionCard, HtmlElement> cardToHtmlMapper) {
        if (pile.isEmpty()) {
            return HtmlElement.swapInnerHtml(targetId);
        }

        return cardToHtmlMapper.apply(pile.getLast());
    }
}
