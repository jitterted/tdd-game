package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Player;

import java.util.stream.Stream;

public class PlayerViewComponent {
    private final Player player;

    public PlayerViewComponent(Player player) {
        this.player = player;
    }

    public String generateHtmlAsYou() {
        HtmlComponent workspaceDiv = new HtmlComponent.DivHtmlComponent("workspace",
                                                                        new HtmlComponent.TextComponent("<h2>Workspace</h2>"));
        HtmlComponent handComponent = new HtmlComponent.DivHtmlComponent("hand",
                                                                         createDivsForEach(player.hand()));
        HtmlComponent handContainerDiv = new HtmlComponent.DivHtmlComponent("titled-container",
                                                                            new HtmlComponent.TextComponent("Your Hand"),
                                                                            handComponent);
        return new HtmlComponent.SwapComponent(workspaceDiv, handContainerDiv)
                .render();
    }

    static HtmlComponent[] createDivsForEach(Stream<ActionCard> actionCards) {
        return actionCards
                .map(card -> new HtmlComponent.DivHtmlComponent("card",
                                                                new HtmlComponent.TextComponent(card.title())))
                .toList()
                .toArray(new HtmlComponent[0]);
    }

}
