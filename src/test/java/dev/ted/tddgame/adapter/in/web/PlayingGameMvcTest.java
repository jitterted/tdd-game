package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.TddGameConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayingGame.class)
@Import(TddGameConfig.class)
@Tag("mvc")
@WithMockUser(username = "yellowUsername")
class PlayingGameMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getToGameEndpointReturns200() throws Exception {
        mockMvc.perform(get("/game/gameHandle"))
               .andExpect(status().isOk());
    }
}