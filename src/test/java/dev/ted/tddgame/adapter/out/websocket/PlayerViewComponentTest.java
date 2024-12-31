package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.EventEnqueuer;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerId;
import io.github.ulfs.assertj.jsoup.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerViewComponentTest {

    private static final EventEnqueuer DUMMY_EVENT_ENQUEUER = _ -> {};

    @Test
    void generateHtmlWithSwapIdTargetForPlayerWithNoCards() {
        Player player = createPlayer(79L, "Player with no cards");

        String generatedHtml = new PlayerViewComponent(player).generateHtmlAsYou();

        assertThat(generatedHtml)
                .isEqualTo("""
                           <swap id="you" hx-swap-oob="innerHTML">
                               <div class="workspace">
                                   <h2>Workspace</h2>
                               </div>
                               <div class="titled-container">
                                   Your Hand
                                   <div class="hand">
                                      \s
                                   </div>
                               </div>
                           </swap>
                           """);
    }

    @Test
    @Disabled("Until no cards test passes")
    void generateHtmlWithOneDivForPlayerWithOneCards() {
        Player player = createPlayer(34L, "Player with one PREDICT card");
        player.apply(new PlayerDrewActionCard(player.memberId(),
                                              ActionCard.LESS_CODE));

        String generatedHtml = new PlayerViewComponent(player).generateHtmlAsYou();

        Assertions.assertThatDocument(generatedHtml)
                  .elementContainsText("swap#your-hand > div.card", "less code");

        assertThat(generatedHtml)
                .isEqualTo("""
                           <swap id="your-hand" hx-swap-oob="innerHTML">
                               <div class="card">less code</div>
                           </swap>
                           """);
    }

    @Test
    @Disabled("Until generate with one card passes")
    void generateHtmlForPlayerWithFiveCards() {
        Player player = createPlayer(56L, "Player Name");
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.PREDICT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.PREDICT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.LESS_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.WRITE_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.REFACTOR));

        String generatedHtml = new PlayerViewComponent(player).generateHtmlAsYou();

        assertThat(generatedHtml)
                .isEqualTo("""
                           <swap id="your-hand" hx-swap-oob="innerHTML">
                               <div class="card">predict</div>
                               <div class="card">predict</div>
                               <div class="card">less code</div>
                               <div class="card">write code</div>
                               <div class="card">refactor</div>
                           </swap>
                           """);
    }

    private Player createPlayer(long playerId, String playerName) {
        return new Player(new PlayerId(playerId), new MemberId(42L), playerName, DUMMY_EVENT_ENQUEUER);
    }
}