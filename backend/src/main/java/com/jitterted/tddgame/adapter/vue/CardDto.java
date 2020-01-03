package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayingCard;
import lombok.Data;

@Data
public class CardDto {
  private final String title;
  private final int id;

  public static CardDto from(PlayingCard playingCard) {
    return new CardDto(playingCard.title(), playingCard.id().getId());
  }

}
