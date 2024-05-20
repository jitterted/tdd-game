package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.PersonId;
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
        return new GameJoiner(new PlayerJoinsGame());
    }

    @PostMapping("/join")
    public String joinGame(Principal principal,
                                  @RequestParam("gameHandle") String gameHandle) {
        playerJoinsGame.join(new Person(new PersonId(42L)), gameHandle);
        return "redirect:/";
    }
}
