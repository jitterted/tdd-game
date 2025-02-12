package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.ActionCard;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameScenarioBuilderTest {

    @Test
    void definedActionCardsDealtToPlayerInOrderAfterGameStarted() {
        GameScenarioBuilder builder = GameScenarioBuilder
                .create()
                .actionCards(
                        ActionCard.WRITE_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.PREDICT,
                        ActionCard.PREDICT)
                .memberJoinsAsOnlyPlayer()
                .startGame();

        assertThat(builder.firstPlayer().hand())
                .containsExactly(ActionCard.WRITE_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.PREDICT,
                                 ActionCard.PREDICT
                );
    }

    @Test
    void discardMovesCardFromFirstPlayerHandToDiscardPile() {
        GameScenarioBuilder builder = GameScenarioBuilder
                .create()
                .actionCards(4, ActionCard.WRITE_CODE,
                             1, ActionCard.LESS_CODE)
                .memberJoinsAsOnlyPlayer()
                .startGame()
                .discard(ActionCard.LESS_CODE);

        assertThat(builder.firstPlayer().hand())
                .doesNotContain(ActionCard.LESS_CODE);
    }

    @Test
    void onePairActionCardsCreatesCorrectNumberOfCards() {
        GameScenarioBuilder builder = GameScenarioBuilder
                .create()
                .actionCards(5, ActionCard.WRITE_CODE)
                .memberJoinsAsOnlyPlayer()
                .startGame();

        assertThat(builder.firstPlayer().hand())
                .containsExactly(ActionCard.WRITE_CODE,
                                 ActionCard.WRITE_CODE,
                                 ActionCard.WRITE_CODE,
                                 ActionCard.WRITE_CODE,
                                 ActionCard.WRITE_CODE);
    }

    @Test
    void twoPairActionCardsCreatesCorrectNumberOfCards() {
        GameScenarioBuilder builder = GameScenarioBuilder
                .create()
                .actionCards(2, ActionCard.WRITE_CODE,
                             3, ActionCard.LESS_CODE)
                .memberJoinsAsOnlyPlayer()
                .startGame();

        assertThat(builder.firstPlayer().hand())
                .containsExactly(ActionCard.WRITE_CODE,
                                 ActionCard.WRITE_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.LESS_CODE);
    }

    @Test
    void threePairActionCardsCreatesCorrectNumberOfCards() {
        GameScenarioBuilder builder = GameScenarioBuilder
                .create()
                .actionCards(2, ActionCard.WRITE_CODE,
                             2, ActionCard.LESS_CODE,
                             1, ActionCard.PREDICT)
                .memberJoinsAsOnlyPlayer()
                .startGame();

        assertThat(builder.firstPlayer().hand())
                .containsExactly(ActionCard.WRITE_CODE,
                                 ActionCard.WRITE_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.PREDICT);
    }
}
