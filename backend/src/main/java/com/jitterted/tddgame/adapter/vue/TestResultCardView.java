package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.TestResultCard;
import lombok.Data;

@Data
public class TestResultCardView {
  private final int id;
  private final String title;

  public static TestResultCardView of(TestResultCard testResultCard) {
    return new TestResultCardView(testResultCard.id().getId(), testResultCard.title());
  }
}
