package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.GameStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;

@Controller
public class PlayingGame {

    private final GameStore gameStore;

    public PlayingGame(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    @GetMapping("/game/{gameHandle}")
    public String game(Model model,
                       @PathVariable("gameHandle") String gameHandle) {
        model.addAttribute("gameView", new GameView("handle", Collections.emptyList()));
        return "game";
    }
}
