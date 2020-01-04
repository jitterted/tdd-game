package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.CardId;
import com.jitterted.tddgame.domain.GameService;
import com.jitterted.tddgame.domain.Hand;
import com.jitterted.tddgame.domain.Player;
import com.jitterted.tddgame.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// thanks to LiveCodingWithSch3lp for the "Contract" name (instead of "Integration")
class GameControllerContractTest {

  private static final String API_PLAYERS_BASE_URL = "/api/game/players/";
  @Autowired
  MockMvc mockMvc;

  @Autowired
  GameService gameService;

  @Test
  public void getAgainstApiGameIs200Ok() throws Exception {
    Player player1 = gameService.currentGame().players().get(0);
    player1.assignUser(new User("player1"));
    Player player2 = gameService.currentGame().players().get(1);
    player2.assignUser(new User("player2"));

    int playerId = player1.id().getId();
    mockMvc.perform(get(API_PLAYERS_BASE_URL + playerId))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.hand").exists())
           .andExpect(jsonPath("$.inPlay").exists())
           .andExpect(jsonPath("$.opponentInPlay").exists())
           .andExpect(jsonPath("$.opponentName").exists())
           .andExpect(jsonPath("$.name").exists())
    ;
  }

  @Test
  public void postOfCardToDiscardsIs200Ok() throws Exception {
    Player player = gameService.currentGame().players().get(0);
    int cardId = player.hand().cards().get(0).id().getId();
    mockMvc.perform(
      post(API_PLAYERS_BASE_URL + player.id().getId() + "/discards")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\": " + cardId + ",  \"source\": \"hand\"" + "}")
    )
           .andExpect(status().isOk())
    ;
  }

  @Test
  public void postPlayCardActionIs200Ok() throws Exception {
    Player player = gameService.currentGame().players().get(0);
    int cardId = player.hand().cards().get(0).id().getId();

    mockMvc.perform(
      post(API_PLAYERS_BASE_URL + player.id().getId() + "/plays")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\": " + cardId + "}")
    )
           .andExpect(status().isOk())
    ;
  }

  @Test
  public void postDrawCardActionIs200Ok() throws Exception {
    Player player = gameService.currentGame().players().get(0);
    Hand hand = player.hand();
    CardId cardId = hand.cards().get(0).id();
    hand.remove(cardId); // make room in hand for draw from deck

    mockMvc.perform(
      post(API_PLAYERS_BASE_URL + player.id().getId() + "/actions")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"action\": \"" + PlayerAction.DRAW_CARD + "\"}")
    )
           .andExpect(status().isOk())
    ;
  }

  @Test
  public void postDrawTestResultsActionIs200Ok() throws Exception {
    Player player = gameService.currentGame().players().get(0);

    mockMvc.perform(
      post(API_PLAYERS_BASE_URL + player.id().getId() + "/test-result-card-draws")
    )
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").exists())
           .andExpect(jsonPath("$.title").exists())
    ;

  }
}
