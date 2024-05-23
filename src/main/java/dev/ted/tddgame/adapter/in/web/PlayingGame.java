package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Player;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PlayingGame {

    private final GameStore gameStore;

    public PlayingGame(GameStore gameStore) {
        this.gameStore = gameStore;
    }

    @GetMapping("/game/{gameHandle}")
    public String game(Model model,
                       @PathVariable("gameHandle") String gameHandle) {
        List<Player> playerViews = gameStore
                .findByHandle(gameHandle)
                .orElseThrow()
                .players();
        model.addAttribute("gameView", new GameView(gameHandle, PlayerView.from(playerViews)));
        return "game";
    }
}
