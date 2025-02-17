package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;

public class CardViewComponent {

    static HtmlElement imgElementFor(ActionCard card) {
        return HtmlElement.img("/" + baseImageFilenameOf(card.title()) + ".png", card.title());
    }

    static String baseImageFilenameOf(String cardTitle) {
        return cardTitle.toLowerCase()
                        .replace(" ", "-")
                        .replace("'", "");
    }
}
