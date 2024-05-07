package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.TestResultCard;

public class TestResultCardView {
    private final int id;
    private final String title;

    public TestResultCardView(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static TestResultCardView of(TestResultCard testResultCard) {
        return new TestResultCardView(testResultCard.id().getId(), testResultCard.title());
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TestResultCardView)) {
            return false;
        }
        final TestResultCardView other = (TestResultCardView) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TestResultCardView;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        return result;
    }

    public String toString() {
        return "TestResultCardView(id=" + this.getId() + ", title=" + this.getTitle() + ")";
    }
}
