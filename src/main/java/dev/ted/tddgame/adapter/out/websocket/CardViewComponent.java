package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Card;
import dev.ted.tddgame.domain.TestResultsCard;

import java.util.Map;

public class CardViewComponent<C extends Card> {
    private static final Map<String, String> CARD_NAME_TO_FILENAME = Map.of(
            TestResultsCard.AS_PREDICTED.name(), "/as-predicted.png"
            , TestResultsCard.NEED_ONE_LESS_CODE.name(), "/need-one-less-code.png"
            , TestResultsCard.NEED_TWO_LESS_CODE.name(), "/need-two-less-code.png"
            , ActionCard.LESS_CODE.name(), "/less-code.png"
            , ActionCard.PREDICT.name(), "/predict.png"
            , ActionCard.WRITE_CODE.name(), "/write-code.png"
            , ActionCard.REFACTOR.name(), "/refactor.png"
            , ActionCard.CANT_ASSERT.name(), "/cant-assert.png"
            , ActionCard.CODE_BLOAT.name(), "/code-bloat.png"
    );
    private final C card;

    private CardViewComponent(C card) {
        this.card = card;
    }

    public static CardViewComponent<ActionCard> of(ActionCard card) {
        return new CardViewComponent<>(card);
    }

    public static CardViewComponent<TestResultsCard> of(TestResultsCard card) {
        return new CardViewComponent<>(card);
    }

    static HtmlElement.HtmlAttributes htmlAttributesFor(String gameHandle, ActionCard card) {
        return HtmlElement.attributes()
                          .cssClass("card")
                          .hxGet("/game/" + gameHandle + "/card-menu/" + card.name())
                          .hxSwap("none")
                          .hxOn("after-settle",
                                "document.querySelector('dialog').showModal()");
    }

    static HtmlElement asButton(String gameHandle, ActionCard card) {
        return HtmlElement.button(htmlAttributesFor(gameHandle, card),
                                  of(card).html());
    }

    public HtmlElement html() {
        return HtmlElement.img(filenameFor(card), card.title());
    }

    private String filenameFor(C card) {
        return CARD_NAME_TO_FILENAME.get(card.name());
    }

}
