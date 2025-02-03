package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.Workspace;

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
                      .flatMap(player -> workspaceElementsOf(player.workspace()))
                      .reduce(forest(), HtmlElement::addChildren);
    }

    private Stream<HtmlElement> workspaceElementsOf(Workspace workspace) {
        String hexTileName = workspace.currentHexTile().title().toLowerCase().replace(" ", "-").replace("'", "").replace("?", "");
        String currentHexTileHtmlId = "%s-hex-tile".formatted(hexTileName);
        String pawnTargetIdForSwap = "workspace%d-pawn".formatted(workspace.id().id() + 1);
        String classNamesForPawnIcon = "fa-solid fa-circle-%d".formatted(workspace.id().id() + 1);
        return Stream.of(
                swapDelete(pawnTargetIdForSwap),
                swapBeforeEnd(currentHexTileHtmlId)
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

    public HtmlElement htmlForInPlayCards(Workspace workspace) {
        return HtmlElement.swapInnerHtml(
                "your-in-play",
                inPlayCardDivs(workspace)
        );
    }

    private HtmlElement[] inPlayCardDivs(Workspace workspace) {
        return workspace.cardsInPlay()
                        .map(card -> div(
                                "card",
                                HtmlElement.img(
                                        "/write-code.png",
                                        "Write Code"
                                )
                        ))
                        .toList()
                        .toArray(HtmlElement[]::new);
    }

}