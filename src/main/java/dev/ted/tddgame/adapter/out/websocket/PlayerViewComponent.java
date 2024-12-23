package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

public class PlayerViewComponent {

    // Constructor takes the Player whose point of view we use when generating HTML

    public String generateHtmlFor(Player player) {
        return """
               <swap id="your-hand" hx-swap-oob="innerHTML">%s</swap>
               """.formatted("""
                             
                                 <div class="card">PREDICT</div>
                             """);
    }
}
