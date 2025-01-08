package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static dev.ted.tddgame.adapter.out.websocket.HtmlComponent.div;
import static dev.ted.tddgame.adapter.out.websocket.HtmlComponent.swapInnerHtml;
import static dev.ted.tddgame.adapter.out.websocket.HtmlComponent.text;
import static org.assertj.core.api.Assertions.*;

class OtherPlayersViewComponentTest {

    @Test
    void otherPlayersHandsHtmlForAllPlayersInStartedGame() {
        Game game = Game.create("irrelevant game name", "gameHandle");
        MemberId memberIdForOliver = new MemberId(78L);
        game.join(memberIdForOliver, "Oliver");
        MemberId memberIdForSamantha = new MemberId(63L);
        game.join(memberIdForSamantha, "Samantha");
        Player oliverPlayer = game.playerFor(memberIdForOliver);
        Player samanthaPlayer = game.playerFor(memberIdForSamantha);

        game.start();

        HtmlComponent htmlComponentActual = new OtherPlayersViewComponent(game)
                .htmlForOtherPlayers();
        // actual is a ForestHtmlComponent that contains:
        // 2 swap-innerHTML components, 1 for each player's hand
        //  for each of those, has a DIV container (to hold the cards)
        //      which contains 5 DIVs (one for each card)
        // <h2 class="name">Player Name for ID of 1</h2>
        // <div class="other-player-container">
        //     <div class="workspace">
        //         Workspace
        //     </div>
        //     <div class="titled-container">
        //         Hand
        //         <div class="hand">
        //             <div class="card">predict</div>
        //             <div class="card">predict</div>
        //             <div class="card">less code</div>
        //             <div class="card">write code</div>
        //             <div class="card">refactor</div>
        //         </div>
        //     </div>
        // </div>
        HtmlComponent.Swap oliverSwap =
                swapInnerHtml("player-id-" + oliverPlayer.id().id(),
                              text("<h2 class=\"name\">Oliver</h2>"),
                              div("other-player-container",
                                  div("titled-container",
                                      text("Hand"),
                                      new HandViewComponent(oliverPlayer).handDiv())
                              ));
        HtmlComponent.Swap samanthaSwap =
                swapInnerHtml("player-id-" + samanthaPlayer.id().id(),
                              text("<h2 class=\"name\">Samantha</h2>"),
                              div("other-player-container",
                                  div("titled-container",
                                      text("Hand"),
                                      new HandViewComponent(samanthaPlayer).handDiv())
                              ));

        assertThat(htmlComponentActual)
                .isEqualTo(new HtmlComponent.Forest(
                        oliverSwap,
                        samanthaSwap
                ));
    }

}