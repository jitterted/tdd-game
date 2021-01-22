package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.DrawnTestResultCard;
import com.jitterted.tddgame.domain.Game;
import com.jitterted.tddgame.domain.GameStateChannel;
import com.jitterted.tddgame.domain.PlayerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StompGameStateChannel implements GameStateChannel {
  private static final String TOPIC_TESTRESULTCARD = "/topic/testresultcard";
  private static final String TOPIC_GAMESTATE = "/topic/gamestate";

  private final AtomicInteger messageNumberSequence = new AtomicInteger(0);
  private final SimpMessagingTemplate simpMessagingTemplate;

  private final TaskExecutor taskExecutor;

  @Autowired
  public StompGameStateChannel(SimpMessagingTemplate simpMessagingTemplate, TaskExecutor taskExecutor) {
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.taskExecutor = taskExecutor;
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

  @Override
  public void playerActed(Game game) {
    taskExecutor.execute(() -> {
      GameStateChangedEvent gameStateChangedEvent = GameStateChangedEvent.from(game);
      Map<String, Object> messageNumberHeader = Map.of("message-number",
                                                       messageNumberSequence.getAndIncrement());
      simpMessagingTemplate.convertAndSend(TOPIC_GAMESTATE,
                                           gameStateChangedEvent,
                                           messageNumberHeader);
    });
  }
}
