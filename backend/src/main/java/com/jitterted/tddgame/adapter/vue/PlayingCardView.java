package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayingCard;

public class PlayingCardView {
    private final String title;
    private final int id;

    public PlayingCardView(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public static PlayingCardView from(PlayingCard playingCard) {
        return new PlayingCardView(playingCard.title(), playingCard.id().getId());
    }

    public String getTitle() {
        return this.title;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayingCardView)) {
            return false;
        }
        final PlayingCardView other = (PlayingCardView) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayingCardView;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        result = result * PRIME + this.getId();
        return result;
    }

    public String toString() {
        return "PlayingCardView(title=" + this.getTitle() + ", id=" + this.getId() + ")";
    }
}
