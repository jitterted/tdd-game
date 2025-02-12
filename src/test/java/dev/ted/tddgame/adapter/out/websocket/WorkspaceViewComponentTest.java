package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewTechNeglectCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class WorkspaceViewComponentTest {

    private static final MemberId IRRELEVANT_MEMBER_ID = new MemberId(42L);
    private static final long IRRELEVANT_PLAYER_ID = 32L;

    @Test
    void emptyPlayAreaInWorkspaceRenderedAsEmpty() {
        Player player = Player.createNull(22L, "Player no in-play cards");

        WorkspaceViewComponent workspaceViewComponent =
                new WorkspaceViewComponent(List.of(player));

        HtmlElement htmlElement = workspaceViewComponent
                .htmlForInPlayCardsForYou(player.workspace());
        assertThat(htmlElement)
                .isEqualTo(HtmlElement.swapInnerHtml(
                        WorkspaceViewComponent.YOUR_IN_PLAY_HTML_ID)
                );
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


    @Test
    void emptyTechNeglectAreaInWorkspaceRenderedAsEmpty() {
        Player player = Player.createNull(23L, "Player no Tech Neglect cards");

        WorkspaceViewComponent workspaceViewComponent =
                new WorkspaceViewComponent(List.of(player));

        HtmlElement htmlElement = workspaceViewComponent
                .htmlForTechNeglectCardsForYou(player.workspace());

        assertThat(htmlElement)
                .isEqualTo(HtmlElement.swapInnerHtml(
                        WorkspaceViewComponent.YOUR_TECH_NEGLECT_HTML_ID
                ));
    }

    @Test
    void twoCardsInTechNeglectAreaInWorkspaceRenderedAsTwoCardDivs() {
        Player player = Player.createNull(IRRELEVANT_PLAYER_ID,
                                          "Player with two Tech Neglect cards");
        player.apply(new PlayerDrewTechNeglectCard(IRRELEVANT_MEMBER_ID,
                                                   ActionCard.CODE_BLOAT));
        player.apply(new PlayerDrewTechNeglectCard(IRRELEVANT_MEMBER_ID,
                                                   ActionCard.CANT_ASSERT));

        WorkspaceViewComponent workspaceViewComponent =
                new WorkspaceViewComponent(List.of(player));

        HtmlElement htmlElement = workspaceViewComponent
                .htmlForTechNeglectCardsForYou(player.workspace());

        assertThat(htmlElement)
                .isEqualTo(HtmlElement.swapInnerHtml(
                        WorkspaceViewComponent.YOUR_TECH_NEGLECT_HTML_ID,
                        HtmlElement.div(
                                "card",
                                HtmlElement.img(
                                        "/code-bloat.png",
                                        "Code Bloat"
                                )
                        ),
                        HtmlElement.div(
                                "card",
                                HtmlElement.img(
                                        "/cant-assert.png",
                                        "Can't Assert"
                                )
                        )
                ));
    }
}