package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.Player;

import java.util.List;

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
        HtmlElement[] swapHtmlForAllPawns = new HtmlElement[players.size()];
        int index = 0;
        for (Player player : players) {
            String workspaceId = String.valueOf(player.id().id());

            swapHtmlForAllPawns[index++] =
                    forest(
                            swapDelete("workspace" + workspaceId + "-pawn"),
                            swapBeforeEnd("what-should-it-do-hex-tile",
                                          div("workspace" + workspaceId + "-pawn", "hex-tile-stack-pawn",
                                              faIcon("fa-regular fa-circle-1")
                                          )
                            ));
        }
        return forest(swapHtmlForAllPawns);
    }

}
