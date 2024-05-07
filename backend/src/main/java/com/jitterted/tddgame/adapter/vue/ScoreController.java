package com.jitterted.tddgame.adapter.vue;

import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ScoreController {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(ScoreController.class);

  @MessageMapping("/score")
  public String handle(String scoreState) {
    log.info("Handling Score State message \"{}\"", scoreState);
    return scoreState;
  }
}
