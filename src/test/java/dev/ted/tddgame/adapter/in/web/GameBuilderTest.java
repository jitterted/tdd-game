package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.ActionCard;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameBuilderTest {

    @Test
    @Disabled("Until we determine Game startup and deck creation")
    void actionCardsDefineDrawOrderOfActionsCards() {
        GameBuilder builder = GameBuilder.create()
                                         .actionCards(
                                                 ActionCard.WRITE_CODE,
                                                 ActionCard.LESS_CODE,
                                                 ActionCard.LESS_CODE,
                                                 ActionCard.PREDICT,
                                                 ActionCard.PREDICT);

        assertThat(builder.game().actionCardDeck().discardPile())
                .containsExactly(ActionCard.WRITE_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.LESS_CODE,
                                 ActionCard.PREDICT,
                                 ActionCard.PREDICT
                );
    }
}