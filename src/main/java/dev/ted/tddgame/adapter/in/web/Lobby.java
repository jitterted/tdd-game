package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.GameView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Lobby {

    private final List<GameView> gameViews = new ArrayList<>();

    public Lobby() {
    }

    public Lobby(List<GameView> gameViews) {
        this.gameViews.addAll(gameViews);
    }

    public static Lobby createNull() {
        return new Lobby();
    }

    public static Lobby createNull(GameView gameView) {
        return new Lobby(List.of(gameView));
    }

    @GetMapping("/")
    public String redirectToLobby() {
        return "redirect:/lobby";
    }

    @GetMapping("/lobby")
    public String showLobby(Principal principal, Model model) {
        model.addAttribute("personName", principal.getName());
        model.addAttribute("gameViews", gameViews);
        return gameViews.isEmpty() ? "no-game-lobby" : "lobby";
    }

    @PostMapping("/games")
    public String hostNewGame(Principal principal,
                              @RequestParam("newGameName") String newGameName) {
        GameView newGame = new GameView(newGameName, "", -1);
        gameViews.add(newGame);
        return "redirect:/lobby";
    }

}
