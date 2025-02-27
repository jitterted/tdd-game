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
                .map(card -> CardViewComponent.asButton(gameHandle, card))
                .toArray(HtmlElement[]::new);
    }

}
