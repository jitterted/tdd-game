package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

import java.util.stream.Collectors;

public class PlayerViewComponent {
    private final Player player;

    public PlayerViewComponent(Player player) {
        this.player = player;
    }

    public String generateHtml() {
        return """
               <swap id="your-hand" hx-swap-oob="innerHTML">%s</swap>
               """.formatted(yourCardsAsHtml(this.player));
    }

    private String yourCardsAsHtml(Player player) {
        return player.hand()
                     .map(card -> """
                                      <div class="card">%s</div>
                                  """.formatted(card.title()))
                     .collect(Collectors.joining("", "\n", ""));
    }

}
