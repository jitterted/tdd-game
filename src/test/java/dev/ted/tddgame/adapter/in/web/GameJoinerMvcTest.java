package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.TddGameConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameJoiner.class)
@Import(TddGameConfig.class)
@Tag("mvc")
@WithMockUser(username = "Blue")
class GameJoinerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void postToJoinGameRedirects() throws Exception {
        mockMvc.perform(post("/join")
                                .param("gameHandle", "345")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }

}