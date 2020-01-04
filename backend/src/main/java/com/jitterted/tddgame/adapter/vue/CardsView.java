package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayingCard;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class CardsView {
  private final List<PlayingCardView> cards;

  public static CardsView from(List<PlayingCard> playingCards) {
    List<PlayingCardView> playingCardViews = playingCards.stream()
                                                         .map(PlayingCardView::from)
                                                         .collect(Collectors.toList());
    return new CardsView(playingCardViews);
  }

  public static CardsView of(PlayingCard... playingCards) {
    List<PlayingCardView> playingCardViews = Stream.of(playingCards)
                                                   .map(PlayingCardView::from)
                                                   .collect(Collectors.toList());
    return new CardsView(playingCardViews);
  }
}
