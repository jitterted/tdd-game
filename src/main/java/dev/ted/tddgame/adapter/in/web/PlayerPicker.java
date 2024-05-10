package dev.ted.tddgame.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PlayerPicker {

    @PostMapping("/pickaplayer")
    public String pickPlayer() {
        return "redirect:/lobby";
    }

}
