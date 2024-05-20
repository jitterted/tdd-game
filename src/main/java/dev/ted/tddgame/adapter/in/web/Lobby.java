package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.GameCreator;
import dev.ted.tddgame.application.PlayerJoinsGame;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameView;
import dev.ted.tddgame.domain.Person;
import dev.ted.tddgame.domain.PersonId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class Lobby {

    private final GameStore gameStore;
    private final GameCreator gameCreator;
    private PlayerJoinsGame playerJoinsGame;

    @Autowired
    public Lobby(GameStore gameStore, GameCreator gameCreator, PlayerJoinsGame playerJoinsGame) {
        this.gameStore = gameStore;
        this.gameCreator = gameCreator;
        this.playerJoinsGame = playerJoinsGame;
    }

    public static Lobby create(GameStore gameStore) {
        return new Lobby(gameStore, GameCreator.create(gameStore), new PlayerJoinsGame());
    }

    public static Lobby createNull() {
        GameStore gameStore = new GameStore();
        return create(gameStore);
    }

    public static Lobby createNull(Game game) {
        GameStore gameStore = new GameStore();
        gameStore.save(game);
        return create(gameStore);
    }

    @GetMapping("/")
    public String redirectToLobby() {
        return "redirect:/lobby";
    }

    @GetMapping("/lobby")
    public String showLobby(Principal principal, Model model) {
        model.addAttribute("personName", principal.getName());

        List<Game> allGames = gameStore.findAll();
        if (allGames.isEmpty()) {
            return "no-game-lobby";
        }

        model.addAttribute("gameViews", GameView.from(allGames));
        return "lobby";
    }

    @PostMapping("/games")
    public String hostNewGame(Principal principal,
                              @RequestParam("newGameName") String newGameName) {
        gameCreator.createNewGame(newGameName);
        return "redirect:/lobby";
    }

    @PostMapping("/join")
    public String joinGame(Principal principal,
                           @RequestParam("gameHandle") String gameHandle) {
        playerJoinsGame.join(new Person(new PersonId(42L)), gameHandle);
        return "redirect:/";
    }
}
