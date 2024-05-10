package dev.ted.tddgame.adapter.in.web;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerPicker.class)
@Tag("mvc")
class PlayerPickerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void postToPickPlayerEndpointRedirects() throws Exception {
        mockMvc.perform(post("/pickaplayer")
                                .param("person", "Blue"))
               .andExpect(status().is3xxRedirection());
    }
}