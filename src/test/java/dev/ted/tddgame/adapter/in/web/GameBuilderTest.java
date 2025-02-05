package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.ActionCard;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameBuilderTest {

    @Test
    void definedActionCardsDealtToPlayerInOrderAfterGameStarted() {
        GameBuilder builder = GameBuilder
                .create()
                .actionCards(
                        ActionCard.WRITE_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.LESS_CODE,
                        ActionCard.PREDICT,
                        ActionCard.PREDICT)
                .memberJoinsAsPlayer()
                .startGame();

        assertThat(builder.firstPlayer().hand())
                .containsExactly(ActionCard.WRITE_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.PREDICT,
                                 ActionCard.PREDICT
                );
    }
}