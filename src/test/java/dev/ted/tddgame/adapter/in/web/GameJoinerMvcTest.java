package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.TddGameConfig;
import dev.ted.tddgame.application.GameCreator;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
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
@WithMockUser(username = "blueUsername")
class GameJoinerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GameCreator gameCreator;

    @Autowired
    MemberStore memberStore;

    @Test
    void postToJoinGameRedirects() throws Exception {
        Game game = gameCreator.createNewGame("Game Name");
        memberStore.save(new Member(new MemberId(99L), "nicknameBlue", "blueUsername"));
        mockMvc.perform(post("/join")
                                .param("gameHandle", game.handle())
                                .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }

}