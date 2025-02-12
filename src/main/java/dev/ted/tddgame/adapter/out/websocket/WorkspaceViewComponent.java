package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
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
    static final String YOUR_IN_PLAY_HTML_ID = "your-in-play";
    public static final String YOUR_TECH_NEGLECT_HTML_ID = "your-tech-neglect";
    private final List<Player> players;

    public WorkspaceViewComponent(List<Player> players) {
        this.players = players;
    }

    HtmlElement htmlForPawns() {
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

    public HtmlElement htmlForInPlayCardsForYou(Workspace workspace) {
        return HtmlElement.swapInnerHtml(
                YOUR_IN_PLAY_HTML_ID,
                cardsAsDivs(workspace.cardsInPlay())
        );
    }

    public HtmlElement htmlForTechNeglectCardsForYou(Workspace workspace) {
        return HtmlElement.swapInnerHtml(
                WorkspaceViewComponent.YOUR_TECH_NEGLECT_HTML_ID,
                cardsAsDivs(workspace.techNeglectCards())
        );
    }

    private HtmlElement[] cardsAsDivs(Stream<ActionCard> cards) {
        return cards
                .map(card -> div(
                        "card",
                        HandViewComponent.imgElementFor(card)
                ))
                .toList()
                .toArray(HtmlElement[]::new);
    }

}