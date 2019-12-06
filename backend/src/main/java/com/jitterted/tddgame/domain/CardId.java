package com.jitterted.tddgame.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardId {
  private int id;

  public static CardId of(int id) {
    return new CardId(id);
  }

}
