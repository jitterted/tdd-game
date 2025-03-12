package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

class WorkspaceTest {

    @Test
    void workspaceRemembersLastTestResultsCardDrawn() {
        Workspace workspace = Workspace.createForTest(HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE);

    }
}