package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.GameStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class PlayingGame {

    private final GameStore gameStore;

    public PlayingGame(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    @GetMapping("/game") // RESUME HERE: add Game Handle as path parameter, redirecting from
    public String game(Model model) {
        model.addAttribute("gameView", new GameView("handle", Collections.emptyList()));
        return "game";
    }
}
