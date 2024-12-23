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

        String generatedHtml = new PlayerViewComponent().generateHtmlFor(player);

        assertThat(generatedHtml)
                .isEqualTo("""
                           <swap id="your-hand" hx-swap-oob="innerHTML"></swap>
                           """);
    }

    @Test
    void generateHtmlWithOneDivForPlayerWithOneCards() {
        Player player = createPlayer(34L, "Player with one PREDICT card");
        player.apply(new PlayerDrewActionCard(player.memberId(),
                                              ActionCard.PREDICT));

        String generatedHtml = new PlayerViewComponent().generateHtmlFor(player);

        Assertions.assertThatDocument(generatedHtml)
                          .elementContainsText("swap#your-hand > div.card", "PREDICT");

        assertThat(generatedHtml)
                .isEqualTo("""
                           <swap id="your-hand" hx-swap-oob="innerHTML">
                               <div class="card">PREDICT</div>
                           </swap>
                           """);
    }

    @Disabled("First do zero and one, then this 'many'")
    @Test
    void generateHtmlForPlayerWithFiveCards() {
        Player player = createPlayer(56L, "Player Name");

        String generatedHtml = new PlayerViewComponent().generateHtmlFor(player);


        String expectedHtml = """
                              <div class="big-hand-card">PREDICT</div>
                              <div class="big-hand-card">PREDICT</div>
                              <div class="big-hand-card">LESS CODE</div>
                              <div class="big-hand-card">WRITE CODE</div>
                              <div class="big-hand-card">REFACTOR</div>
                              """;
    }

    private Player createPlayer(long playerId, String playerName) {
        return new Player(new PlayerId(playerId), new MemberId(42L), playerName, DUMMY_EVENT_ENQUEUER);
    }
}