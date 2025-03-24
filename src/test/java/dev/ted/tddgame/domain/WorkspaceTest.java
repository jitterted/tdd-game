package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
                .isThrownBy(() -> workspace.testResultsCardDrawn(TestResultsCard.AS_PREDICTED))
                .withMessage("Not allowed to draw another Test Results card if the current one in the workspace hasn't been processed (and discarded).");
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

    @ParameterizedTest
    @CsvSource(textBlock =
            """
            NEED_ONE_LESS_CODE,0
            NEED_TWO_LESS_CODE,0
            NEED_TWO_LESS_CODE,1
            """
    )
    @Disabled("dev.ted.tddgame.domain.WorkspaceTest 3/13/25 12:17 — until we complete successful prediction")
    void hexTileUnchangedWhenHaveFewerLessCodeCardsThanTestResultsCardNeeds(
            String drawnTestResultsCardString,
            int numberOfLessCodeCardsPlayed) {
        Workspace workspace = Workspace.createForTest();
        workspace.cardDiscarded();
        workspace.cardDiscarded();
        workspace.cardPlayed(ActionCard.WRITE_CODE);
        for (int i = 0; i < numberOfLessCodeCardsPlayed; i++) {
            workspace.cardPlayed(ActionCard.LESS_CODE);
        }
        workspace.cardPlayed(ActionCard.PREDICT);
        TestResultsCard drawnTestResultsCard = TestResultsCard.valueOf(drawnTestResultsCardString);
        workspace.testResultsCardDrawn(drawnTestResultsCard);

        workspace.processTestResultsCard();

        assertThat(workspace.drawnTestResultsCard())
                .isNull();
        assertThat(workspace.currentHexTile())
                .isEqualByComparingTo(HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE);
        // check cards in-play: only PREDICT should have been discarded
    }

    @ParameterizedTest
    @CsvSource(textBlock =
            """
            AS_PREDICTED,0
            AS_PREDICTED,1
            AS_PREDICTED,2
            NEED_ONE_LESS_CODE,1
            NEED_ONE_LESS_CODE,2
            NEED_TWO_LESS_CODE,2
            """
    )
    @Disabled("dev.ted.tddgame.domain.WorkspaceTest 3/13/25 12:48 — until we figure out what's needed from the Game/Player level")
    void movesToNextHexTileWhenTestResultsCardAsExpected(
            String drawnTestResultsCardString,
            int numberOfLessCodeCardsPlayed) {
        Workspace workspace = Workspace.createForTest();
        workspace.cardDiscarded();
        workspace.cardDiscarded();
        workspace.cardPlayed(ActionCard.WRITE_CODE);
        for (int i = 0; i < numberOfLessCodeCardsPlayed; i++) {
            workspace.cardPlayed(ActionCard.LESS_CODE);
        }
        workspace.cardPlayed(ActionCard.PREDICT);
        TestResultsCard drawnTestResultsCard = TestResultsCard.valueOf(drawnTestResultsCardString);
        workspace.testResultsCardDrawn(drawnTestResultsCard);

        workspace.processTestResultsCard();

        assertThat(workspace.drawnTestResultsCard())
                .as("Test Results Card should have been discarded, but was not")
                .isNull();
        assertThat(workspace.currentHexTile())
                .as("Should have moved to the next hex tile upon successful prediction")
                .isEqualByComparingTo(HexTile.WRITE_CODE_SO_TEST_COMPILES);
        assertThat(workspace.cardsInPlay())
                .as("Cards In Play should have been discarded, but they weren't")
                .isEmpty();
    }

    @Test
    void exceptionThrownWhenProcessTestResultsCardWithNoCurrentCardDrawn() {

    }
}