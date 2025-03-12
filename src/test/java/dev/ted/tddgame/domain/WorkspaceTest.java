package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static dev.ted.tddgame.domain.TestResultsCard.AS_PREDICTED;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
class WorkspaceTest {

    @Test
    void newWorkspaceHasNoTestResultsCardDrawn() {
        Workspace workspace = new Workspace(new PlayerId(37L));

        assertThat(workspace.drawnTestResultsCard())
                .isNull();
    }

    @Test
    void workspaceRemembersLastTestResultsCardDrawn() {
        Workspace workspace = Workspace.createForTest(HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE);

        workspace.testResultsCardDrawn(AS_PREDICTED);

        assertThat(workspace.drawnTestResultsCard())
                .as("Last Drawn Test Results Card should be known by the Workspace")
                .isEqualByComparingTo(AS_PREDICTED);
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
                .isNull();
    }

    @Test
    // parameterize:
    // need 2, have 0
    // need 2, have 1
    // need 1, have 0
    void hexTileUnchangedWhenHaveFewerLessCodeCardsThanTestResultsCardNeeds() {

    }

    @Test
    // AS_EXPECTED, (doesn't matter how many LESS CODE cards you have)
    // NEED 1, have 1
    // NEED 1, have 2 <-
    // NEED 2, have 2
    void movesToNextHexTileWhenTestResultsCardAsExpected() {
        Workspace workspace = Workspace.createForTest();
        workspace.cardDiscarded();
        workspace.cardDiscarded();
        workspace.cardPlayed(ActionCard.WRITE_CODE);
        workspace.cardPlayed(ActionCard.LESS_CODE);
        workspace.cardPlayed(ActionCard.LESS_CODE);
        workspace.cardPlayed(ActionCard.PREDICT);
        workspace.testResultsCardDrawn(TestResultsCard.NEED_ONE_LESS_CODE);

        workspace.processTestResultsCard();

        assertThat(workspace.drawnTestResultsCard())
                .isNull();
        assertThat(workspace.currentHexTile())
                .isEqualByComparingTo(HexTile.WRITE_CODE_SO_TEST_COMPILES);
    }

    @Test
    void exceptionThrownWhenProcessTestResultsCardWithNoCurrentCardDrawn() {

    }
}