package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.EventEnqueuer;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerId;
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
    void generateHtmlWithOneDivForPlayerWithOneCards() {
        Player player = createPlayer(34L, "Player with one LESS CODE card");
        player.apply(new PlayerDrewActionCard(player.memberId(),
                                              ActionCard.LESS_CODE));

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
                                       <div class="card">less code</div>
                                   </div>
                               </div>
                           </swap>
                           """);
    }

    @Test
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
                           <swap id="you" hx-swap-oob="innerHTML">
                               <div class="workspace">
                                   <h2>Workspace</h2>
                               </div>
                               <div class="titled-container">
                                   Your Hand
                                   <div class="hand">
                                       <div class="card">predict</div>
                                       <div class="card">predict</div>
                                       <div class="card">less code</div>
                                       <div class="card">write code</div>
                                       <div class="card">refactor</div>
                                   </div>
                               </div>
                           </swap>
                           """);
    }

    private Player createPlayer(long playerId, String playerName) {
        return new Player(new PlayerId(playerId), new MemberId(42L), playerName, DUMMY_EVENT_ENQUEUER);
    }
}