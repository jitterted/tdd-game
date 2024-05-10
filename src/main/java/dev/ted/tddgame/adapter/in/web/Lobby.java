package dev.ted.tddgame.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class Lobby {

    @GetMapping("/")
    public String redirectToLobby() {
        return "redirect:/lobby";
    }

    @GetMapping("/lobby")
    public String showLobby(Principal principal, Model model) {
        model.addAttribute("personName", principal.getName());
        return "lobby";
    }

    @PostMapping("/games")
    public String hostNewGame(Principal principal, @RequestParam("newGameName") String newGameName) {
        return "redirect:/lobby";
    }

}
