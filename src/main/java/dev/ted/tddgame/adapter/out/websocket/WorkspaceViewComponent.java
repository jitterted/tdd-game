package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.Player;

import java.util.List;
import java.util.stream.Stream;

import static dev.ted.tddgame.adapter.HtmlElement.div;
import static dev.ted.tddgame.adapter.HtmlElement.faIcon;
import static dev.ted.tddgame.adapter.HtmlElement.forest;
import static dev.ted.tddgame.adapter.HtmlElement.swapBeforeEnd;
import static dev.ted.tddgame.adapter.HtmlElement.swapDelete;

public class WorkspaceViewComponent {
    private final List<Player> players;

    public WorkspaceViewComponent(List<Player> players) {
        this.players = players;
    }

    HtmlElement getHtmlForPawns() {
        return players.stream()
                      .flatMap(this::workspaceElementsForPlayer)
                      .reduce(forest(), HtmlElement::addChildren);
    }

    private Stream<HtmlElement> workspaceElementsForPlayer(Player player) {
        long workspaceId = player.id().id();
        String currentHexTile = "what-should-it-do-hex-tile";
        String pawnTargetIdForSwap = "workspace" + workspaceId + "-pawn";
        String classNamesForPawnIcon = "fa-regular fa-circle-1";
        return Stream.of(
                swapDelete(pawnTargetIdForSwap),
                swapBeforeEnd(currentHexTile)
                        .addChildren(
                                div()
                                        .id(pawnTargetIdForSwap)
                                        .classNames("hex-tile-stack-pawn")
                                        .addChildren(
                                                faIcon(classNamesForPawnIcon)
                                        )
                        )
        );
    }

}
