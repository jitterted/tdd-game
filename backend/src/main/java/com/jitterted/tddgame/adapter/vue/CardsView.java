package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Card;
import com.jitterted.tddgame.domain.Hand;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class CardsView {
  private final List<CardDto> cards;

  public static CardsView from(Hand hand) {
    List<CardDto> cardDtos = hand.cards()
                                 .stream()
                                 .map(CardDto::from)
                                 .collect(Collectors.toList());
    return new CardsView(cardDtos);
  }

  public static CardsView of(Card... cards) {
    List<CardDto> cardDtos = Stream.of(cards)
                                   .map(CardDto::from)
                                   .collect(Collectors.toList());
    return new CardsView(cardDtos);
  }
}
