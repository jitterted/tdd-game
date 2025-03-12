package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

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

        workspace.testResultsCardDrawn(TestResultsCard.AS_PREDICTED);

        assertThat(workspace.drawnTestResultsCard())
                .as("Last Drawn Test Results Card should be known by the Workspace")
                .contains(TestResultsCard.AS_PREDICTED);
    }
}