package com.jitterted.tddgame.domain;

import org.springframework.lang.NonNull;

public class Card {
  private final String title;
  private final CardId id;

  public Card(@NonNull CardId id, @NonNull String title) {
    this.title = title;
    this.id = id;
  }

  public String title() {
    return title;
  }

  public CardId id() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    return id.equals(card.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "CARD(" + id.getId() + "): \"" + title + "\"";
  }
}
