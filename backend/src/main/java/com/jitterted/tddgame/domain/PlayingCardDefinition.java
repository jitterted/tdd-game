package com.jitterted.tddgame.domain;

public enum PlayingCardDefinition {
  LESS_CODE("less code", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF),
  WRITE_CODE("write code", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF),
  REFACTOR("refactor", OnDrawGoesTo.HAND, OnPlayGoesTo.DISCARD),
  PREDICT("predict", OnDrawGoesTo.HAND, OnPlayGoesTo.SELF),

  // "tech debt" (aka "red") cards
  CODE_BLOAT("code bloat", OnDrawGoesTo.IN_PLAY, OnPlayGoesTo.SELF),
  CANT_ASSERT("can't assert", OnDrawGoesTo.IN_PLAY, OnPlayGoesTo.SELF),
  ;

  private final String title;
  private final OnDrawGoesTo onDrawGoesTo;
  private final OnPlayGoesTo onPlayGoesTo;

  PlayingCardDefinition(String title, OnDrawGoesTo onDrawGoesTo, OnPlayGoesTo onPlayGoesTo) {
    this.title = title;
    this.onDrawGoesTo = onDrawGoesTo;
    this.onPlayGoesTo = onPlayGoesTo;
  }

  public String title() {
    return title;
  }

  public OnDrawGoesTo onDrawGoesTo() {
    return onDrawGoesTo;
  }

  public OnPlayGoesTo onPlayGoesTo() {
    return onPlayGoesTo;
  }
}
