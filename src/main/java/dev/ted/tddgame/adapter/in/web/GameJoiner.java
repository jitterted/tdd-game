package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.domain.MemberId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class GameJoiner {
    private final PlayerJoinsGame playerJoinsGame;

    @Autowired
    public GameJoiner(PlayerJoinsGame playerJoinsGame) {
        this.playerJoinsGame = playerJoinsGame;
    }

    static GameJoiner createNull() {
        return new GameJoiner(PlayerJoinsGame.createNull());
    }

    @PostMapping("/join")
    public String joinGame(Principal principal,
                           @RequestParam("gameHandle") String gameHandle) {
        // look up Member in MemberStore using principal.getName()
        playerJoinsGame.join(new MemberId(42L), gameHandle);
        return "redirect:/game-in-progress";
    }
}
