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
                      .flatMap(player -> workspaceElementsFor(player.id().id()))
                      .reduce(forest(), HtmlElement::addChildren);
    }

    private static Stream<HtmlElement> workspaceElementsFor(long workspaceId) {
        return Stream.of(
                swapDelete("workspace" + workspaceId + "-pawn"),
                swapBeforeEnd("what-should-it-do-hex-tile")
                        .addChildren(
                                div()
                                        .id("workspace" + workspaceId + "-pawn")
                                        .classNames("hex-tile-stack-pawn")
                                        .addChildren(
                                                faIcon("fa-regular fa-circle-1")
                                        )
                        )
        );
    }

}
