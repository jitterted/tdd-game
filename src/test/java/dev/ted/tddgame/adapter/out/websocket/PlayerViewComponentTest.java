package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.ted.tddgame.adapter.HtmlElement.div;
import static dev.ted.tddgame.adapter.HtmlElement.h2;
import static dev.ted.tddgame.adapter.HtmlElement.swapInnerHtml;
import static dev.ted.tddgame.adapter.HtmlElement.text;
import static org.assertj.core.api.Assertions.*;

class PlayerViewComponentTest {

    @Test
    void generateHtmlWithSwapIdTargetForPlayerWithNoCards() {
        Player player = Player.createNull(79L, "Player with no cards");

        HtmlElement htmlElement =
                new PlayerViewComponent("ZZZ-game-handle", player).htmlForYou();
        assertThat(htmlElement)
                .isEqualTo(
                        swapInnerHtml("you",
                                      emptyWorkspace(),
                                      div()
                                              .classNames("titled-container")
                                              .addChildren(
                                                      text("Your Hand"),
                                                      div().classNames("hand")
                                              )
                        )
                );
    }

    @Test
    void generateHtmlWithOneDivForPlayerWithOneCards() {
        Player player = Player.createNull(34L, "Player with one LESS CODE card");
        player.apply(new PlayerDrewActionCard(player.memberId(),
                                              ActionCard.LESS_CODE));
        String gameHandle = "test-game-handle";

        HtmlElement htmlElement = new PlayerViewComponent(gameHandle, player).htmlForYou();

        assertThat(htmlElement)
                .isEqualTo(
                        swapInnerHtml("you",
                                      emptyWorkspace(),
                                      div("titled-container",
                                          text("Your Hand"),
                                          new HandViewComponent(gameHandle, player).handContainer()
                                      )
                        )
                );
    }

    @Test
    void generateHtmlForPlayerWithFiveCards() {
        Player player = Player.createNull(56L, "Player Name");
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.PREDICT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.PREDICT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.LESS_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.WRITE_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.REFACTOR));
        String gameHandle = "test-game-handle";

        HtmlElement htmlElement = new PlayerViewComponent(gameHandle, player).htmlForYou();

        assertThat(htmlElement)
                .isEqualTo(
                        swapInnerHtml("you",
                                      emptyWorkspace(),
                                      div("titled-container",
                                          text("Your Hand"),
                                          new HandViewComponent(gameHandle, player).handContainer()
                                      )
                        )
                );

    }

    @Test
    void createsPlaceholderDivForOnlyOtherPlayers() {
        Player you = Player.createNull(99L, "You as Player 99");
        List<Player> players = List.of(Player.createNull(3L, "Name of Player 3"),
                                       you,
                                       Player.createNull(5L, "Name of Player 5"));

        HtmlElement htmlElement = new PlayerViewComponent("ZZZ-game-handle", you)
                .htmlPlaceholdersForOtherPlayers(players);

        assertThat(htmlElement)
                .isEqualTo(
                        swapInnerHtml("other-players",
                                      div("player-id-3", "other-player-container",
                                          h2("Name of Player 3").classNames("name")
                                      ),
                                      div("player-id-5", "other-player-container",
                                          h2("Name of Player 5").classNames("name")
                                      )
                        )
                );
    }

    static HtmlElement emptyWorkspace() {
        return div().classNames("workspace")
                    .addChildren(
                            h2("Workspace"),
                            div().classNames("in-play")
                                 .id(WorkspaceViewComponent.YOUR_IN_PLAY_HTML_ID),
                            div().classNames("tech-neglect")
                                 .id(WorkspaceViewComponent.YOUR_TECH_NEGLECT_HTML_ID)
                    );
    }

}