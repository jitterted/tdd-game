package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.DrawnTestResultCard;

public class TestResultCardDrawnEvent {
    private final String action = "TestResultCardDrawn";
    private final String playerId;
    private final TestResultCardView testResultCardView;

    public TestResultCardDrawnEvent(DrawnTestResultCard drawnTestResultCard) {
        playerId = String.valueOf(drawnTestResultCard.player().id().getId());
        testResultCardView = TestResultCardView.of(drawnTestResultCard.card());
    }

    public String getAction() {
        return this.action;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public TestResultCardView getTestResultCardView() {
        return this.testResultCardView;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TestResultCardDrawnEvent)) {
            return false;
        }
        final TestResultCardDrawnEvent other = (TestResultCardDrawnEvent) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$action = this.getAction();
        final Object other$action = other.getAction();
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) {
            return false;
        }
        final Object this$playerId = this.getPlayerId();
        final Object other$playerId = other.getPlayerId();
        if (this$playerId == null ? other$playerId != null : !this$playerId.equals(other$playerId)) {
            return false;
        }
        final Object this$testResultCardView = this.getTestResultCardView();
        final Object other$testResultCardView = other.getTestResultCardView();
        if (this$testResultCardView == null ? other$testResultCardView != null : !this$testResultCardView.equals(other$testResultCardView)) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TestResultCardDrawnEvent;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $action = this.getAction();
        result = result * PRIME + ($action == null ? 43 : $action.hashCode());
        final Object $playerId = this.getPlayerId();
        result = result * PRIME + ($playerId == null ? 43 : $playerId.hashCode());
        final Object $testResultCardView = this.getTestResultCardView();
        result = result * PRIME + ($testResultCardView == null ? 43 : $testResultCardView.hashCode());
        return result;
    }

    public String toString() {
        return "TestResultCardDrawnEvent(action=" + this.getAction() + ", playerId=" + this.getPlayerId() + ", testResultCardView=" + this.getTestResultCardView() + ")";
    }
}
