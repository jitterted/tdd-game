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
        HtmlComponent workspaceDiv = new HtmlComponent
                .Div("workspace",
                     new HtmlComponent.Text("<h2>Workspace</h2>"));
        HtmlComponent handComponent = new HtmlComponent
                .Div("hand",
                     createDivsForEachCardIn(player.hand()));
        HtmlComponent handContainerDiv = new HtmlComponent
                .Div("titled-container",
                     new HtmlComponent.Text("Your Hand"),
                     handComponent);
        String targetId = "you";
        return HtmlComponent.swapInnerHtml(targetId, workspaceDiv, handContainerDiv)
                .render();
    }

    static HtmlComponent[] createDivsForEachCardIn(Stream<ActionCard> actionCards) {
        return actionCards
                .map(card -> new HtmlComponent.Div("card",
                                                   new HtmlComponent.Text(card.title())))
                .toList()
                .toArray(new HtmlComponent[0]);
    }

    public HtmlComponent htmlForOtherPlayers() {
        return null;
    }
}
