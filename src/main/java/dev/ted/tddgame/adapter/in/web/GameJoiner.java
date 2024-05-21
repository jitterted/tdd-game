package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class GameJoiner {
    private final PlayerJoinsGame playerJoinsGame;
    private final MemberStore memberStore;

    @Autowired
    public GameJoiner(PlayerJoinsGame playerJoinsGame, MemberStore memberStore) {
        this.playerJoinsGame = playerJoinsGame;
        this.memberStore = memberStore;
    }

    static GameJoiner createNull() {
        return new GameJoiner(PlayerJoinsGame.createNull(), new MemberStore());
    }

    @PostMapping("/join")
    public String joinGame(Principal principal,
                           @RequestParam("gameHandle") String gameHandle) {
        Member member = memberStore.findByAuthName(principal.getName())
                                     .orElseThrow(() -> new RuntimeException("Member not found with authName: " + principal.getName()));
        playerJoinsGame.join(member.id(), gameHandle, member.nickname());
        return "redirect:/game-in-progress";
    }
}
