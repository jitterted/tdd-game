package dev.ted.tddgame.adapter.in.web;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Lobby.class)
@Tag("mvc")
@WithMockUser("Blue")
class LobbyMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getToRootPathRedirectsToLobby() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/lobby"));
    }

    @Test
    void getToLobbyIsStatus200Ok() throws Exception {
        mockMvc.perform(get("/lobby"))
               .andExpect(status().isOk());
    }
}