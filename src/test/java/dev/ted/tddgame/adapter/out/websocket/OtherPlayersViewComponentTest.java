package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.adapter.in.web.GameScenarioBuilder;
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
    void otherPlayersHandAndWorkspaceHtmlForAllPlayersInStartedGame() {
        MemberId oliverMemberId = new MemberId(78L);
        MemberId samanthaMemberId = new MemberId(63L);
        GameScenarioBuilder gameScenarioBuilder = GameScenarioBuilder
                .create("other-game-handle")
                .actionCards(
                        // Oliver's cards
                        ActionCard.LESS_CODE,
                        ActionCard.REFACTOR,
                        ActionCard.WRITE_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.CODE_BLOAT, // tech debt does not end up in hand, but in their Workspace
                        ActionCard.CODE_BLOAT, // tech debt does not end up in hand, but in their Workspace
                        ActionCard.PREDICT,
                        // Samantha's cards
                        ActionCard.LESS_CODE,
                        ActionCard.PREDICT,
                        ActionCard.WRITE_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.CANT_ASSERT, // tech debt does not end up in hand, but in their Workspace
                        ActionCard.CANT_ASSERT, // tech debt does not end up in hand, but in their Workspace
                        ActionCard.PREDICT
                )
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
        Player oliverPlayer = gameScenarioBuilder.playerFor(oliverMemberId);
        Player samanthaPlayer = gameScenarioBuilder.playerFor(samanthaMemberId);

        HtmlElement htmlElementActual = new OtherPlayersViewComponent(gameScenarioBuilder.game())
                .htmlForOtherPlayers();
        HtmlElement oliverSwap =
                swapInnerHtml(
                        "player-id-" + oliverPlayer.id().id(),
                        h2("Oliver (Player)").classNames("name"),
                        div().classNames("other-player-container")
                             .addChildren(
                                     text("Workspace"),
                                     div().classNames("workspace")
                                          .addChildren(
                                                  div().classNames("in-play")
                                                       .addChildren(
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            CardViewComponent.imgElementFor(ActionCard.WRITE_CODE)
                                                                    ),
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            CardViewComponent.imgElementFor(ActionCard.LESS_CODE)
                                                                    ),
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            CardViewComponent.imgElementFor(ActionCard.PREDICT)
                                                                    )
                                                       ),
                                                  div().classNames("tech-neglect")
                                                       .addChildren(
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            CardViewComponent.imgElementFor(ActionCard.CODE_BLOAT)
                                                                    ),
                                                               div().classNames("card")
                                                                    .addChildren(
                                                                            CardViewComponent.imgElementFor(ActionCard.CODE_BLOAT)
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
                                           text("Workspace"),
                                           div().classNames("workspace")
                                                .addChildren(
                                                        div().classNames("in-play")
                                                             .addChildren(
                                                                     div().classNames("card")
                                                                          .addChildren(
                                                                                  CardViewComponent.imgElementFor(ActionCard.WRITE_CODE)
                                                                          )
                                                             ),
                                                        div().classNames("tech-neglect")
                                                             .addChildren(
                                                                     div().classNames("card")
                                                                          .addChildren(
                                                                                  CardViewComponent.imgElementFor(ActionCard.CANT_ASSERT)
                                                                          ),
                                                                     div().classNames("card")
                                                                          .addChildren(
                                                                                  CardViewComponent.imgElementFor(ActionCard.CANT_ASSERT)
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

        HtmlElement.Forest expectedForest = new HtmlElement.Forest(
                oliverSwap,
                samanthaSwap
        );
        assertThat(htmlElementActual)
                .isEqualTo(expectedForest);
    }

}