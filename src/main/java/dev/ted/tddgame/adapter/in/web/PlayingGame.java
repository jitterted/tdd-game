package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Player;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class PlayingGame {

    private final GameStore gameStore;
    private final GamePlay gamePlay;

    public PlayingGame(GameStore gameStore, GamePlay gamePlay) {
        this.gameStore = gameStore;
        this.gamePlay = gamePlay;
    }

    @GetMapping("/game/{gameHandle}")
    public String game(Model model,
                       @PathVariable("gameHandle") String gameHandle) {
        List<Player> playerViews = gameStore
                .findByHandle(gameHandle)
                .orElseThrow(() -> new RuntimeException("Game '%s' not found"
                        .formatted(gameHandle)))
                .players();
        model.addAttribute("gameView", new GameView(gameHandle, PlayerView.from(playerViews)));
        return "game";
    }

    @PostMapping("/game/{gameHandle}/start-game")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable("gameHandle") String gameHandle) {
         gamePlay.start(gameHandle);
    }

    // GET menu

    // POST execute command from menu
}
