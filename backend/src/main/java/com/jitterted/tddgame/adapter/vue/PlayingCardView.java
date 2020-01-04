package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayingCard;
import lombok.Data;

@Data
public class PlayingCardView {
  private final String title;
  private final int id;

  public static PlayingCardView from(PlayingCard playingCard) {
    return new PlayingCardView(playingCard.title(), playingCard.id().getId());
  }

}
