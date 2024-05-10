package dev.ted.tddgame.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlayerPicker {

    @PostMapping("/pickaplayer")
    public String pickPlayer(@RequestParam("person") String playerName) {

        return "redirect:/lobby";
    }

}
