package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.adapter.in.web.GameBuilder;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Test;

import static dev.ted.tddgame.adapter.HtmlElement.div;
import static dev.ted.tddgame.adapter.HtmlElement.h2;
import static dev.ted.tddgame.adapter.HtmlElement.swapInnerHtml;
import static dev.ted.tddgame.adapter.HtmlElement.text;
import static org.assertj.core.api.Assertions.*;

class OtherPlayersViewComponentTest {

    @Test
    void otherPlayersHandsHtmlForAllPlayersInStartedGame() {
        MemberId oliverMemberId = new MemberId(78L);
        MemberId samanthaMemberId = new MemberId(63L);
        GameBuilder gameBuilder = GameBuilder
                .create("other-game-handle")
                .actionCards(ActionCard.WRITE_CODE, ActionCard.WRITE_CODE,
                             ActionCard.LESS_CODE, ActionCard.LESS_CODE,
                             ActionCard.LESS_CODE, ActionCard.LESS_CODE,
                             ActionCard.PREDICT, ActionCard.PREDICT,
                             ActionCard.REFACTOR, ActionCard.REFACTOR)
                .memberJoinsAsPlayer(oliverMemberId, "Oliver", "Oliver (authName)", "Oliver (Player)")
                .memberJoinsAsPlayer(samanthaMemberId, "Samantha", "Samantha (authName)", "Samantha (Player)")
                .startGame()
                .playerActions(oliverMemberId, executor -> {
                    executor.discard(ActionCard.LESS_CODE);
                    executor.discard(ActionCard.REFACTOR);
                    executor.playCard(ActionCard.WRITE_CODE);
                })
                .playerActions(samanthaMemberId, executor -> {
                    executor.discard(ActionCard.LESS_CODE);
                    executor.discard(ActionCard.PREDICT);
                    executor.playCard(ActionCard.WRITE_CODE);
                })
                .playerActions(oliverMemberId, executor -> {
                    executor.playCard(ActionCard.LESS_CODE);
                    executor.playCard(ActionCard.PREDICT);
                });
        Player oliverPlayer = gameBuilder.playerFor(oliverMemberId);
        Player samanthaPlayer = gameBuilder.playerFor(samanthaMemberId);

        HtmlElement htmlElementActual = new OtherPlayersViewComponent(gameBuilder.game())
                .htmlForOtherPlayers();
        // actual is a ForestHtmlComponent that contains:
        // 2 swap-innerHTML components, 1 for each player's hand
        //  for each of those, has a DIV container (to hold the cards)
        //      which contains 5 DIVs (one for each card)
        // <h2 class="name">Player Name for ID of 1</h2>
        // <div class="other-player-container">
        //     Workspace
        //     <div class="workspace">
        //         <div class="in-play">
        //             <div class="card">
        //                 <img src="/write-code.png">
        //             </div>
        //             <div class="card">
        //                 <img src="/less-code.png">
        //             </div>
        //             <div class="card">
        //                 <img src="/predict.png">
        //             </div>
        //         </div>
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
        HtmlElement oliverSwap =
                swapInnerHtml(
                        "player-id-" + oliverPlayer.id().id(),
                        h2("Oliver (Player)").classNames("name"),
                        div().classNames("other-player-container")
                             .addChildren(
                                     div().classNames("workspace")
                                          .addChildren(
                                                  div().classNames("in-play")
                                                       .addChildren(
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            HandViewComponent.imgElementFor(ActionCard.WRITE_CODE)
                                                                    ),
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            HandViewComponent.imgElementFor(ActionCard.LESS_CODE)
                                                                    ),
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            HandViewComponent.imgElementFor(ActionCard.PREDICT)
                                                                    )
                                                       )
                                          ),
                                     div().classNames("titled-container")
                                          .addChildren(
                                                  text("Hand"),
                                                  new HandViewComponent("other-game-handle", oliverPlayer)
                                                          .handContainer()
                                          )
                             )
                );
        HtmlElement samanthaSwap =
                swapInnerHtml("player-id-" + samanthaPlayer.id().id(),
                              h2("Samantha (Player)").classNames("name"),
                              div().classNames("other-player-container")
                                   .addChildren(
                                           div().classNames("workspace")
                                                .addChildren(
                                                        div().classNames("in-play")
                                                             .addChildren(
                                                                     div().classNames("card")
                                                                          .addChildren(
                                                                                  HandViewComponent.imgElementFor(ActionCard.WRITE_CODE)
                                                                          )
                                                             )
                                                ),
                                           div().classNames("titled-container")
                                                .addChildren(
                                                        text("Hand"),
                                                        new HandViewComponent("other-game-handle", samanthaPlayer)
                                                                .handContainer()
                                                )
                                   )
                );

        assertThat(htmlElementActual)
                .isEqualTo(new HtmlElement.Forest(
                        oliverSwap,
                        samanthaSwap
                ));
    }

}