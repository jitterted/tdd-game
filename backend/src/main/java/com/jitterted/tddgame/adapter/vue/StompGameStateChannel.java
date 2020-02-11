package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.DrawnTestResultCard;
import com.jitterted.tddgame.domain.GameStateChannel;
import com.jitterted.tddgame.domain.PlayerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class StompGameStateChannel implements GameStateChannel {
  private static final String TOPIC_TESTRESULTCARD = "/topic/testresultcard";

  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  public StompGameStateChannel(SimpMessagingTemplate simpMessagingTemplate) {
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  @Override
  public void testResultCardDrawn(DrawnTestResultCard drawnTestResultCard) {
    TestResultCardDrawnEvent testResultCardDrawnEvent = new TestResultCardDrawnEvent(drawnTestResultCard);
    simpMessagingTemplate.convertAndSend(TOPIC_TESTRESULTCARD, testResultCardDrawnEvent);
  }

  @Override
  public void testResultCardDiscarded(PlayerId playerId) {
    simpMessagingTemplate.convertAndSend(
      TOPIC_TESTRESULTCARD,
      new TestResultCardDiscardedEvent(playerId));
  }
}
