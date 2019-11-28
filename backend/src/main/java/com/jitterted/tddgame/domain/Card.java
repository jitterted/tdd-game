package com.jitterted.tddgame.domain;

import org.springframework.lang.NonNull;

public class Card {
  private final String title;

  public Card(@NonNull String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    return title.equals(card.title);
  }

  @Override
  public int hashCode() {
    return title.hashCode();
  }

  @Override
  public String toString() {
    return "CARD: " + title;
  }
}
