package com.jitterted.tddgame.domain;

import lombok.Data;

@Data
public class PlayerId {
  private final int id;

  public static PlayerId of(int id) {
    return new PlayerId(id);
  }
}
