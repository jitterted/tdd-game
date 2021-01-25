package com.jitterted.tddgame.domain;

import org.springframework.lang.NonNull;

public class PlayingCard {
  private final String title;
  private final CardId id;
  private final OnPlayGoesTo onPlayGoesTo;
  private final OnDrawGoesTo onDrawGoesTo;

  PlayingCard(@NonNull CardId id, @NonNull String title, @NonNull OnPlayGoesTo onPlayGoesTo, OnDrawGoesTo onDrawGoesTo) {
    this.title = title;
    this.id = id;
    this.onPlayGoesTo = onPlayGoesTo;
    this.onDrawGoesTo = onDrawGoesTo;
  }

  public String title() {
    return title;
  }

  public OnPlayGoesTo usage() {
    return onPlayGoesTo;
  }

  public void onDraw(Hand hand, InPlay inPlay) {
    switch (onDrawGoesTo) {
      case HAND -> hand.add(this);
      case IN_PLAY -> inPlay.add(this);
    }
  }

  public CardId id() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlayingCard playingCard = (PlayingCard) o;

    return id.equals(playingCard.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "PlayingCard (" + id.getId() + "): \"" + title + "\"";
  }
}
