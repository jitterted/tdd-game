package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Player;

import java.util.stream.Stream;

import static dev.ted.tddgame.adapter.out.websocket.HtmlComponent.div;

public class HandViewComponent {

    static HtmlComponent.Div handDivFor(Player player) {
        return div("hand", divsForEachCardIn(player.hand()));
    }

    private static HtmlComponent[] divsForEachCardIn(Stream<ActionCard> actionCards) {
        return actionCards
                .map(card -> div("card", HtmlComponent.text(card.title())))
                .toArray(HtmlComponent[]::new);
    }
}
