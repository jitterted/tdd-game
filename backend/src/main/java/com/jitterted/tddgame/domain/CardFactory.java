package com.jitterted.tddgame.domain;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class CardFactory {
  private final AtomicInteger idSequence = new AtomicInteger(0);

  public PlayingCard playingCard(String title, Usage usage) {
    return new PlayingCard(nextCardId(), title, usage);
  }

  public TestResultCard testResultCard(String title) {
    return new TestResultCard(nextCardId(), title);
  }

  @NotNull
  private CardId nextCardId() {
    return new CardId(idSequence.getAndIncrement());
  }
}
