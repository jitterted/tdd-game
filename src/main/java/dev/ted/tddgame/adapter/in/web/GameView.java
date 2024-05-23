package dev.ted.tddgame.adapter.in.web;

import java.util.List;

public record GameView(String handle, List<PlayerView> playerViews) {
}
