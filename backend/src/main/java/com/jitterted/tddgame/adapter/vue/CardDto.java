package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import lombok.Data;

@Data
public class CardDto {
  private final String title;
  private final int id;

  public static CardDto from(Card card) {
    return new CardDto(card.title(), card.id());
  }

}
