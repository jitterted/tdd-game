package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayingCard;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class CardsView {
  private final List<CardDto> cards;

  public static CardsView from(List<PlayingCard> playingCards) {
    List<CardDto> cardDtos = playingCards.stream()
                                         .map(CardDto::from)
                                         .collect(Collectors.toList());
    return new CardsView(cardDtos);
  }

  public static CardsView of(PlayingCard... playingCards) {
    List<CardDto> cardDtos = Stream.of(playingCards)
                                   .map(CardDto::from)
                                   .collect(Collectors.toList());
    return new CardsView(cardDtos);
  }
}
