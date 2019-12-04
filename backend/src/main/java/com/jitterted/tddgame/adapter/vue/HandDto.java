package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.Hand;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class HandDto {
  private final List<CardDto> cards;

  public static HandDto from(Hand hand) {
    List<CardDto> cardDtos = hand.cards().stream().map(CardDto::from).collect(Collectors.toList());
    return new HandDto(cardDtos);
  }
}
