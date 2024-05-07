package com.jitterted.tddgame.domain;

public class CardId {
    private int id;

    public CardId(int id) {
        this.id = id;
    }

    public CardId() {
    }

    public static CardId of(int id) {
        return new CardId(id);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CardId)) {
            return false;
        }
        final CardId other = (CardId) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CardId;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        return result;
    }

    public String toString() {
        return "CardId(id=" + this.getId() + ")";
    }
}
