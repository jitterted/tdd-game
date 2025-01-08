package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

import java.util.List;
import java.util.function.Predicate;

public class PlayerViewComponent {
    private final Player player;

    public PlayerViewComponent(Player player) {
        this.player = player;
    }

    public String generateHtmlAsYou() {
        HtmlComponent workspaceDiv = new HtmlComponent
                .Div("workspace",
                     new HtmlComponent.Text("<h2>Workspace</h2>"));
        HtmlComponent handComponent = HandViewComponent.handDivFor(player);
        HtmlComponent handContainerDiv = new HtmlComponent
                .Div("titled-container",
                     new HtmlComponent.Text("Your Hand"),
                     handComponent);
        String targetId = "you";
        return HtmlComponent.swapInnerHtml(targetId, workspaceDiv, handContainerDiv)
                            .render();
    }

    public HtmlComponent htmlPlaceholdersForOtherPlayers(List<Player> players) {
        HtmlComponent[] otherDivs = players
                .stream()
                .filter(onlyOtherPlayers())
                .map(PlayerViewComponent::createPlaceholderDiv)
                .toArray(HtmlComponent[]::new);
        return HtmlComponent.swapInnerHtml("other-players", otherDivs);
    }

    private Predicate<Player> onlyOtherPlayers() {
        return player -> !player.equals(this.player);
    }

    private static HtmlComponent.Div createPlaceholderDiv(Player player) {
        return HtmlComponent.div("player-id-" + player.id().id(),
                                 "other-player-container",
                                 HtmlComponent.text("<h2 class=\"name\">" + player.playerName() + "</h2>"));
    }
}
