package dev.ted.tddgame.domain;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DeckFactoryTest {

    @Test
    void createActionCardDeckHasAllActionCardsInDiscardPile() {
        List<ActionCard> actionCards = new DeckFactory().createStandardActionCards();

        assertThat(actionCards)
                .haveExactly(18, actionCardMatching(ActionCard.WRITE_CODE))
                .haveExactly(18, actionCardMatching(ActionCard.LESS_CODE))
                .haveExactly(18, actionCardMatching(ActionCard.PREDICT))
                .haveExactly(2, actionCardMatching(ActionCard.CANT_ASSERT))
                .haveExactly(3, actionCardMatching(ActionCard.CODE_BLOAT))
                .haveExactly(4, actionCardMatching(ActionCard.REFACTOR));
    }

    private static Condition<ActionCard> actionCardMatching(ActionCard actionCard) {
        return new Condition<>(card -> card == actionCard,
                               "%s Action card", actionCard.name());
    }
}