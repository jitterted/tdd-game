
package com.jitterted.tddgame.adapter.vue.masterview;

public class CardDeckView {

  private final int discardPile;
  private final int drawPile;

  public CardDeckView(int discardPile, int drawPile) {
    this.discardPile = discardPile;
    this.drawPile = drawPile;
  }

  public int getDiscardPile() {
    return this.discardPile;
  }

  public int getDrawPile() {
    return this.drawPile;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof CardDeckView)) {
      return false;
    }
    final CardDeckView other = (CardDeckView) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    if (this.getDiscardPile() != other.getDiscardPile()) {
      return false;
    }
    if (this.getDrawPile() != other.getDrawPile()) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof CardDeckView;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + this.getDiscardPile();
    result = result * PRIME + this.getDrawPile();
    return result;
  }

  public String toString() {
    return "CardDeckView(discardPile=" + this.getDiscardPile() + ", drawPile=" + this.getDrawPile() + ")";
  }
}
