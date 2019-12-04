package com.jitterted.tddgame.adapter.vue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// thanks to LiveCodingWithSch3lp for the "Contract" name (instead of "Integration")
class GameControllerContractTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void getAgainstApiGameIs200Ok() throws Exception {
    mockMvc.perform(get("/api/game"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.hand").exists())
           .andExpect(jsonPath("$.inPlay").exists())
           .andExpect(jsonPath("$.opponentInPlay").exists())
    ;
  }

}
