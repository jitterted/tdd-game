package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Player;

import java.util.stream.Stream;

import static dev.ted.tddgame.adapter.HtmlElement.div;

public class HandViewComponent {

    private final Player player;
    private final String gameHandle;

    public HandViewComponent(String gameHandle, Player player) {
        this.gameHandle = gameHandle;
        this.player = player;
    }

    HtmlElement handContainer() {
        return div("hand", buttonsForEachCardIn(player.hand()));
    }

    private HtmlElement[] buttonsForEachCardIn(Stream<ActionCard> actionCards) {
        return actionCards
                .map(this::asButton)
                .toArray(HtmlElement[]::new);
    }

    private HtmlElement.HtmlAttributes htmlAttributesFor(ActionCard card) {
        return HtmlElement.attributes()
                          .cssClass("card")
                          .hxGet("/game/" + gameHandle + "/card-menu/" + card.name())
                          .hxSwap("none")
                          .hxOn("after-settle", "document.querySelector('dialog').showModal()");
    }

    private HtmlElement asButton(ActionCard card) {
        return HtmlElement.button(htmlAttributesFor(card),
                                  imgElementFor(card));
    }

    static HtmlElement imgElementFor(ActionCard card) {
        return HtmlElement.img("/" + baseImageFilenameOf(card.title()) + ".png", card.title());
    }

    static String baseImageFilenameOf(String cardTitle) {
        return cardTitle.toLowerCase()
                        .replace(" ", "-")
                        .replace("'", "");
    }

}
