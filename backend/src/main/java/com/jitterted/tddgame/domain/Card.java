package com.jitterted.tddgame.domain;

import org.springframework.lang.NonNull;

public class Card {
  private final String title;
  private final int id;

  public Card(int id, @NonNull String title) {
    this.title = title;
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    return id == card.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public String toString() {
    return "CARD(" + id + "): " + title;
  }
}
