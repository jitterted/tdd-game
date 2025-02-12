package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.Player;

import java.util.List;
import java.util.function.Predicate;

import static dev.ted.tddgame.adapter.HtmlElement.div;
import static dev.ted.tddgame.adapter.HtmlElement.h2;
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
                div().classNames("workspace")
                     .addChildren(
                             h2("Workspace"),
                             div().classNames("in-play")
                                  .id(WorkspaceViewComponent.YOUR_IN_PLAY_HTML_ID),
                             div().classNames("tech-neglect")
                                  .id(WorkspaceViewComponent.YOUR_TECH_NEGLECT_HTML_ID)
                     );
        HtmlElement handComponent =
                new HandViewComponent(gameHandle, player).handContainer();
        HtmlElement handContainerDiv =
                div().classNames("titled-container")
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
        return div().classNames("other-player-container")
                    .id("player-id-" + player.id().id())
                    .addChildren(
                            h2(player.playerName()).classNames("name")
                    );
    }

}
