package com.jitterted.tddgame.adapter.vue.masterview;

import com.jitterted.tddgame.adapter.vue.CardsView;

public class PlayerWorldView {

    private final String name;
    private final CardsView hand;
    private final CardsView inPlay;

    public PlayerWorldView(String name, CardsView hand, CardsView inPlay) {
        this.name = name;
        this.hand = hand;
        this.inPlay = inPlay;
    }

    public String getName() {
        return this.name;
    }

    public CardsView getHand() {
        return this.hand;
    }

    public CardsView getInPlay() {
        return this.inPlay;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerWorldView)) {
            return false;
        }
        final PlayerWorldView other = (PlayerWorldView) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        final Object this$hand = this.getHand();
        final Object other$hand = other.getHand();
        if (this$hand == null ? other$hand != null : !this$hand.equals(other$hand)) {
            return false;
        }
        final Object this$inPlay = this.getInPlay();
        final Object other$inPlay = other.getInPlay();
        if (this$inPlay == null ? other$inPlay != null : !this$inPlay.equals(other$inPlay)) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerWorldView;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $hand = this.getHand();
        result = result * PRIME + ($hand == null ? 43 : $hand.hashCode());
        final Object $inPlay = this.getInPlay();
        result = result * PRIME + ($inPlay == null ? 43 : $inPlay.hashCode());
        return result;
    }

    public String toString() {
        return "PlayerWorldView(name=" + this.getName() + ", hand=" + this.getHand() + ", inPlay=" + this.getInPlay() + ")";
    }
}
