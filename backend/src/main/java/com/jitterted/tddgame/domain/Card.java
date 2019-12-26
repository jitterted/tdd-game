package com.jitterted.tddgame.domain;

import org.springframework.lang.NonNull;

public class Card {
  private final String title;
  private final CardId id;
  private final Usage usage;

  public Card(@NonNull CardId id, @NonNull String title, @NonNull Usage usage) {
    this.title = title;
    this.id = id;
    this.usage = usage;
  }

  public String title() {
    return title;
  }

  public Usage usage() {
    return usage;
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
