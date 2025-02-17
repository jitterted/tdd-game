package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Card;
import dev.ted.tddgame.domain.TestResultsCard;

import java.util.function.Function;

public class CardViewComponent<C extends Card> {
    private final C card;
    private final Function<C, String> nameExtractor;

    private CardViewComponent(C card, Function<C, String> nameExtractor) {
        this.card = card;
        this.nameExtractor = nameExtractor;
    }

    public static CardViewComponent<ActionCard> of(ActionCard card) {
        return new CardViewComponent<>(card, ActionCard::title);
    }

    public static CardViewComponent<TestResultsCard> of(TestResultsCard card) {
        return new CardViewComponent<>(card, TestResultsCard::name);
    }

    public HtmlElement html() {
        String cardName = nameExtractor.apply(card);
        return HtmlElement.img("/" + filenameFor(cardName) + ".png", cardName);
    }

    private static String filenameFor(String cardTitle) {
        return cardTitle.toLowerCase()
                        .replace(" ", "-")
                        .replace("'", "")
                        .replace("_", "-");
    }
}
