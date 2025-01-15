package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

import java.util.List;
import java.util.function.Predicate;

public class PlayerViewComponent {
    private final Player player;

    public PlayerViewComponent(Player player) {
        this.player = player;
    }

    HtmlElement htmlForYou() {
        HtmlElement workspaceDiv = HtmlElement
                .div("workspace",
                     HtmlElement.text("<h2>Workspace</h2>"));
        HtmlElement handComponent = new HandViewComponent(player).handContainer();
        HtmlElement handContainerDiv = HtmlElement
                .div("titled-container",
                     HtmlElement.text("Your Hand"),
                     handComponent);
        String targetId = "you";
        return HtmlElement.swapInnerHtml(targetId, workspaceDiv, handContainerDiv);
    }

    HtmlElement htmlPlaceholdersForOtherPlayers(List<Player> players) {
        HtmlElement[] otherDivs = players
                .stream()
                .filter(onlyOtherPlayers())
                .map(PlayerViewComponent::createPlaceholderDiv)
                .toArray(HtmlElement[]::new);
        return HtmlElement.swapInnerHtml("other-players", otherDivs);
    }

    private Predicate<Player> onlyOtherPlayers() {
        return player -> !player.equals(this.player);
    }

    private static HtmlElement createPlaceholderDiv(Player player) {
        return HtmlElement.div("player-id-" + player.id().id(),
                               "other-player-container",
                               HtmlElement.text("<h2 class=\"name\">" + player.playerName() + "</h2>"));
    }
}
