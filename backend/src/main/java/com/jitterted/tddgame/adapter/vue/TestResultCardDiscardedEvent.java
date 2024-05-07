package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayerId;

public class TestResultCardDiscardedEvent {
    private final String action = "TestResultCardDiscarded";
    private final String playerId;

    public TestResultCardDiscardedEvent(PlayerId playerId) {
        this.playerId = String.valueOf(playerId.getId());
    }

    public String getAction() {
        return this.action;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TestResultCardDiscardedEvent)) {
            return false;
        }
        final TestResultCardDiscardedEvent other = (TestResultCardDiscardedEvent) o;
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
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TestResultCardDiscardedEvent;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $action = this.getAction();
        result = result * PRIME + ($action == null ? 43 : $action.hashCode());
        final Object $playerId = this.getPlayerId();
        result = result * PRIME + ($playerId == null ? 43 : $playerId.hashCode());
        return result;
    }

    public String toString() {
        return "TestResultCardDiscardedEvent(action=" + this.getAction() + ", playerId=" + this.getPlayerId() + ")";
    }
}
