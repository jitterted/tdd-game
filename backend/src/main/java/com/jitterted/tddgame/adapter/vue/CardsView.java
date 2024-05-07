package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayingCard;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardsView {
  private final List<PlayingCardView> cards;

  public CardsView(List<PlayingCardView> cards) {
    this.cards = cards;
  }

  public static CardsView from(List<PlayingCard> playingCards) {
    List<PlayingCardView> playingCardViews = playingCards.stream()
                                                         .map(PlayingCardView::from)
                                                         .collect(Collectors.toList());
    return new CardsView(playingCardViews);
  }

  public static CardsView of(PlayingCard... playingCards) {
    List<PlayingCardView> playingCardViews = Stream.of(playingCards)
                                                   .map(PlayingCardView::from)
                                                   .collect(Collectors.toList());
    return new CardsView(playingCardViews);
  }

  public List<PlayingCardView> getCards() {
    return this.cards;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof CardsView)) {
      return false;
    }
    final CardsView other = (CardsView) o;
    if (!other.canEqual((Object) this)) {
      return false;
    }
    final Object this$cards = this.getCards();
    final Object other$cards = other.getCards();
    if (this$cards == null ? other$cards != null : !this$cards.equals(other$cards)) {
      return false;
    }
    return true;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof CardsView;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $cards = this.getCards();
    result = result * PRIME + ($cards == null ? 43 : $cards.hashCode());
    return result;
  }

  public String toString() {
    return "CardsView(cards=" + this.getCards() + ")";
  }
}
