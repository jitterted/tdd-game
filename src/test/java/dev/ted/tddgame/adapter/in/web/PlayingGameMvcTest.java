package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.SecurityConfig;
import dev.ted.tddgame.TddGameConfig;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayingGame.class)
@Import({TddGameConfig.class, TestConfig.class, SecurityConfig.class})
@Tag("mvc")
@WithMockUser(username = "irrelevant")
class PlayingGameMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GameStore gameStore;

    @Test
    void getToGameEndpointReturns200() throws Exception {
        gameStore.save(Game.create("Game Name", "gameHandle"));
        mockMvc.perform(get("/game/gameHandle"))
               .andExpect(status().isOk());
    }

    @Test
    @Disabled("Until the Game is created properly with the initial set of Action Cards")
    void postToStartEndpointReturns204() throws Exception {
        mockMvc.perform(post("/game/gameHandle/start-game"))
               .andExpect(status().isNoContent());
    }
}