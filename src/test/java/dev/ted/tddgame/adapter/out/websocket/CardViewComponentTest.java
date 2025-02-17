package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.TestResultsCard;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CardViewComponentTest {

    @Test
    void actionCardWithBasicNameRendersAsExpected() {
        CardViewComponent<ActionCard> component = CardViewComponent.of(ActionCard.PREDICT);

        assertThat(component.html().render())
                .isEqualTo("""
                           <img src="/predict.png" alt="Predict">
                           """);
    }

    @Test
    void actionCardWithSpacesRendersAsExpected() {
        CardViewComponent<ActionCard> component = CardViewComponent.of(ActionCard.LESS_CODE);

        assertThat(component.html().render())
                .isEqualTo("""
                           <img src="/less-code.png" alt="Less Code">
                           """);
    }

    @Test
    void testResultsCardWithBasicNameRendersAsExpected() {
        CardViewComponent<TestResultsCard> component = CardViewComponent.of(TestResultsCard.AS_PREDICTED);

        assertThat(component.html().render())
                .isEqualTo("""
                           <img src="/as-predicted.png" alt="AS_PREDICTED">
                           """);
    }

    @Test
    void testResultsCardWithUnderscoresRendersAsExpected() {
        CardViewComponent<TestResultsCard> component = CardViewComponent.of(TestResultsCard.NEED_ONE_LESS_CODE);

        assertThat(component.html().render())
                .isEqualTo("""
                           <img src="/need-one-less-code.png" alt="NEED_ONE_LESS_CODE">
                           """);
    }
}
