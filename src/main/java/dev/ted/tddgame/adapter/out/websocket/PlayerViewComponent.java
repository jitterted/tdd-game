package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

import java.util.stream.Collectors;

public class PlayerViewComponent {
    private final Player player;

    public PlayerViewComponent(Player player) {
        this.player = player;
    }

    public String generateHtmlAsYou() {
        return """
               <swap id="you" hx-swap-oob="innerHTML">
                   <div class="workspace">
                       <h2>Workspace</h2>
                   </div>
                   <div class="titled-container">
                       Your Hand
                       <div class="hand">
                           %s
                       </div>
                   </div>
               </swap>
               """.formatted(handAsHtml());
    }

    private String handAsHtml() {
        return player.hand()
                     .map(card -> """
                                  <div class="card">%s</div>"""
                             .formatted(card.title()))
                     .collect(Collectors.joining("\n            "));
    }

}
