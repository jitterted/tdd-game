package com.jitterted.tddgame.adapter.vue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ScoreController {
  @MessageMapping("/score")
  public String handle(String scoreState) {
    log.info("Handling Score State message \"{}\"", scoreState);
    return scoreState;
  }
}
