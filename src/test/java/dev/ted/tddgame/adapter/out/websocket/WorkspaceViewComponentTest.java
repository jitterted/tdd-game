package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class WorkspaceViewComponentTest {

    @Test
    void emptyPlayAreaInWorkspaceRenderedAsEmpty() {
        Player player = Player.createNull(22L, "Player no in-play cards");

        WorkspaceViewComponent workspaceViewComponent =
                new WorkspaceViewComponent(List.of(player));

        HtmlElement htmlElement = workspaceViewComponent
                .htmlForInPlayCardsForYou(player.workspace());
        assertThat(htmlElement)
                .isEqualTo(HtmlElement.swapInnerHtml(WorkspaceViewComponent.YOUR_IN_PLAY_HTML_ID));
    }

    @Test
    void playedCardShowsUpInWorkspaceInPlayArea() {
        Player player = Player.createNull(64L, "Player played card");
        player.workspace().cardDiscarded(); // move to tile 2
        player.workspace().cardDiscarded(); // move to tile 3
        player.workspace().cardPlayed(ActionCard.WRITE_CODE);

        WorkspaceViewComponent workspaceViewComponent =
                new WorkspaceViewComponent(List.of(player));

        HtmlElement htmlElement = workspaceViewComponent.htmlForInPlayCardsForYou(player.workspace());
        assertThat(htmlElement)
                .isEqualTo(HtmlElement.swapInnerHtml(
                        WorkspaceViewComponent.YOUR_IN_PLAY_HTML_ID,
                        HtmlElement.div(
                                "card",
                                HtmlElement.img(
                                        "/write-code.png",
                                        "Write Code"
                                )
                        )
                ));
    }

    @Test
    void multiplePlayedCardsShowUpInWorkspaceInPlayArea() {
        Player player = Player.createNull(64L, "Player played card");
        player.workspace().cardDiscarded(); // move to tile 2
        player.workspace().cardDiscarded(); // move to tile 3

        player.workspace().cardPlayed(ActionCard.WRITE_CODE);
        player.workspace().cardPlayed(ActionCard.LESS_CODE);

        WorkspaceViewComponent workspaceViewComponent =
                new WorkspaceViewComponent(List.of(player));

        HtmlElement htmlElement = workspaceViewComponent.htmlForInPlayCardsForYou(player.workspace());
        assertThat(htmlElement)
                .isEqualTo(HtmlElement.swapInnerHtml(
                        WorkspaceViewComponent.YOUR_IN_PLAY_HTML_ID,
                        HtmlElement.div(
                                "card",
                                HtmlElement.img(
                                        "/write-code.png",
                                        "Write Code"
                                )
                        ),
                        HtmlElement.div(
                                "card",
                                HtmlElement.img(
                                        "/less-code.png",
                                        "Less Code"
                                )
                        )
                ));
    }
}