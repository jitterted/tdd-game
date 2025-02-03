package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.Player;

import java.util.List;
import java.util.function.Predicate;

import static dev.ted.tddgame.adapter.HtmlElement.text;

public class PlayerViewComponent {
    private final Player player;
    private final String gameHandle;

    public PlayerViewComponent(String gameHandle, Player player) {
        this.player = player;
        this.gameHandle = gameHandle;
    }

    HtmlElement htmlForYou() {
        HtmlElement workspaceDiv =
                HtmlElement.div()
                           .classNames("workspace")
                           .addChildren(
                                   HtmlElement.text("<h2>Workspace</h2>"),
                                   HtmlElement.div()
                                              .classNames("in-play")
                                              .id(WorkspaceViewComponent.YOUR_IN_PLAY_HTML_ID)
                           );
        HtmlElement handComponent =
                new HandViewComponent(gameHandle, player).handContainer();
        HtmlElement handContainerDiv =
                HtmlElement.div()
                           .classNames("titled-container")
                           .addChildren(
                                   text("Your Hand"),
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
