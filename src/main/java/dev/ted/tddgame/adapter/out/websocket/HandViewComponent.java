package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Player;

import java.util.stream.Stream;

import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.div;

public class HandViewComponent {

    private final Player player;

    public HandViewComponent(Player player) {
        this.player = player;
    }

    HtmlElement handDiv() {
        return div("hand", divsForEachCardIn(player.hand()));
    }

    private HtmlElement[] divsForEachCardIn(Stream<ActionCard> actionCards) {
        return actionCards
                .map(card -> div("card", HtmlElement.text(card.title())))
                .toArray(HtmlElement[]::new);
    }
}
