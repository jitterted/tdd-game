package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

import java.util.stream.Collectors;

public class PlayerViewComponent {

    // Constructor takes the Player whose point of view we use when generating HTML

    public String generateHtmlFor(Player player) {
        return """
               <swap id="your-hand" hx-swap-oob="innerHTML">%s</swap>
               """.formatted(cardsAsHtml(player));
    }

    private String cardsAsHtml(Player player) {
        return player.hand()
                     .map(card -> """
                                      <div class="card">%s</div>
                                  """.formatted(card.title()))
                     .collect(Collectors.joining("", "\n", ""));
    }

}
