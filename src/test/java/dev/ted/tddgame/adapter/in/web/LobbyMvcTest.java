package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.TddGameConfig;
import dev.ted.tddgame.application.PlayerJoinsGame;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Lobby.class)
@Import({TddGameConfig.class, TestConfig.class})
@Tag("mvc")
@WithMockUser(username = "Blue")
class LobbyMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PlayerJoinsGame playerJoinsGame;

    @Test
    void getToRootPathRedirectsToLobby() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/lobby"));
    }

    @Test
    void getToLobbyIsStatus200Ok() throws Exception {
        mockMvc.perform(get("/lobby"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("memberName", "Blue"));
    }

    @Test
    void postToGamesRedirectsToLobby() throws Exception {
        mockMvc.perform(post("/games")
                                .param("newGameName", "666")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/lobby"));
    }

}