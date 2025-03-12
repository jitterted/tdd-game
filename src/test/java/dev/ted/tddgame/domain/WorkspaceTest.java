package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static dev.ted.tddgame.domain.TestResultsCard.AS_PREDICTED;
import static org.assertj.core.api.Assertions.*;

class WorkspaceTest {

    @Test
    void newWorkspaceHasNoTestResultsCardDrawn() {
        Workspace workspace = new Workspace(new PlayerId(37L));

        assertThat(workspace.drawnTestResultsCard())
                .isEmpty();
    }

    @Test
    void workspaceRemembersLastTestResultsCardDrawn() {
        Workspace workspace = Workspace.createForTest(HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE);

        workspace.testResultsCardDrawn(AS_PREDICTED);

        assertThat(workspace.drawnTestResultsCard())
                .as("Last Drawn Test Results Card should be known by the Workspace")
                .contains(AS_PREDICTED);
    }

    @Test
    void exceptionThrownWhenDrawTestResultsCardWithCardCurrentlyDrawn() {
        Workspace workspace = Workspace.createForTest(HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE);
        workspace.testResultsCardDrawn(TestResultsCard.NEED_TWO_LESS_CODE);

        assertThatIllegalStateException()
                .as("Must not be allowed to draw another Test Results card if the current one hasn't been processed.")
                .isThrownBy(() -> workspace.testResultsCardDrawn(TestResultsCard.AS_PREDICTED));
    }

    @Test
    void noTestResultsCardDrawnAvailableAfterCardDrawnAndTestResultsCardProcessed() {
        Workspace workspace = Workspace.createForTest(HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE);
        workspace.testResultsCardDrawn(TestResultsCard.NEED_TWO_LESS_CODE);

        workspace.processTestResultsCard();

        assertThat(workspace.drawnTestResultsCard())
                .as("After processing the drawn card, the current drawn card should be empty.")
                .isEmpty();
    }
}