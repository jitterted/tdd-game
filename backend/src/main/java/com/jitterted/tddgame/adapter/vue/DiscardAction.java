package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.PlayerId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DiscardAction {
  public static final String SOURCE_HAND = "hand";
  public static final String SOURCE_IN_PLAY = "in-play";

  private int id;
  private String source;

  public void executeFor(PlayerId playerId, GameService gameService) {
//    log.debug("Discard Request for Card {}, Source {}, from Player {}", id, source, playerId.getId());
    if (source.equals(SOURCE_HAND)) {
      gameService.currentGame().discardFromHand(playerId, CardId.of(id));
    } else if (source.equals(SOURCE_IN_PLAY)) {
      gameService.currentGame().discardFromInPlay(playerId, CardId.of(id));
    } else {
      throw new UnsupportedOperationException("Unknown source for Discard = \"" + source + "\", id = " + id);
    }
  }
}
