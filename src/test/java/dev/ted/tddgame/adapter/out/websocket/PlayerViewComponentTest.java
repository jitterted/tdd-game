package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.EventEnqueuer;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerId;
import dev.ted.tddgame.domain.Workspace;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.ted.tddgame.adapter.HtmlElement.div;
import static dev.ted.tddgame.adapter.HtmlElement.swapInnerHtml;
import static dev.ted.tddgame.adapter.HtmlElement.text;
import static org.assertj.core.api.Assertions.*;

class PlayerViewComponentTest {

    private static final EventEnqueuer DUMMY_EVENT_ENQUEUER = _ -> {};

    @Test
    void generateHtmlWithSwapIdTargetForPlayerWithNoCards() {
        Player player = createPlayer(79L, "Player with no cards");

        HtmlElement htmlElement = new PlayerViewComponent("ZZZ-game-handle", player).htmlForYou();
        assertThat(htmlElement)
                .isEqualTo(
                        swapInnerHtml("you",
                                      div("workspace",
                                          text("<h2>Workspace</h2>")),
                                      div("titled-container",
                                          text("Your Hand"),
                                          div("hand")
                                      )
                        )
                );
    }

    @Test
    void generateHtmlWithOneDivForPlayerWithOneCards() {
        Player player = createPlayer(34L, "Player with one LESS CODE card");
        player.apply(new PlayerDrewActionCard(player.memberId(),
                                              ActionCard.LESS_CODE));
        String gameHandle = "test-game-handle";

        HtmlElement htmlElement = new PlayerViewComponent(gameHandle, player).htmlForYou();

        assertThat(htmlElement)
                .isEqualTo(
                        swapInnerHtml("you",
                                      div("workspace",
                                          text("<h2>Workspace</h2>")),
                                      div("titled-container",
                                          text("Your Hand"),
                                          new HandViewComponent(gameHandle, player).handContainer()
                                      )
                        )
                );
    }

    @Test
    void generateHtmlForPlayerWithFiveCards() {
        Player player = createPlayer(56L, "Player Name");
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
                                      div("workspace",
                                          text("<h2>Workspace</h2>")),
                                      div("titled-container",
                                          text("Your Hand"),
                                          new HandViewComponent(gameHandle, player).handContainer()
                                      )
                        )
                );

    }

    @Test
    void createsPlaceholderDivForOnlyOtherPlayers() {
        Player you = createPlayer(99L, "You as Player 99");
        List<Player> players = List.of(createPlayer(3L, "Name of Player 3"),
                                       you,
                                       createPlayer(5L, "Name of Player 5"));

        HtmlElement htmlElement = new PlayerViewComponent("ZZZ-game-handle", you)
                .htmlPlaceholdersForOtherPlayers(players);

        assertThat(htmlElement)
                .isEqualTo(
                        swapInnerHtml("other-players",
                                      div("player-id-3", "other-player-container",
                                          text("<h2 class=\"name\">Name of Player 3</h2>")),
                                      div("player-id-5", "other-player-container",
                                          text("<h2 class=\"name\">Name of Player 5</h2>"))));
    }

    private Player createPlayer(long playerId, String playerName) {
        final PlayerId playerId1 = new PlayerId(playerId);
        return new Player(playerId1, new MemberId(42L), playerName, DUMMY_EVENT_ENQUEUER, new Workspace(playerId1));
    }
}