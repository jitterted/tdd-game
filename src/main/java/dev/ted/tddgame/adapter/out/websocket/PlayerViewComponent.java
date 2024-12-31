package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

import java.util.stream.Collectors;

public class PlayerViewComponent {
    private final Player player;

    public PlayerViewComponent(Player player) {
        this.player = player;
    }

    public String generateHtmlAsYou() {
        String html = """
                      <div class="workspace">
                          <h2>Workspace</h2>
                      </div>
                      <div class="titled-container">
                          Your Hand
                          %s
                      </div>
                      """.formatted(handAsHtml());
        return new SwapComponent(() -> html).render();
    }

    private String handAsHtml() {
        return """
               <div class="hand">
               """ +
               player.hand()
                     .map(card -> """
                                          <div class="card">%s</div>
                                  """
                             .formatted(card.title()))
                     .collect(Collectors.joining())
               +
               "    </div>";
    }

    static class SwapComponent implements HtmlComponent {
        private final HtmlComponent htmlComponent;

        public SwapComponent(HtmlComponent htmlComponent) {
            this.htmlComponent = htmlComponent;
        }

        @Override
        public String render() {
            return """
                   <swap id="you" hx-swap-oob="innerHTML">
                   """
                   +
                   htmlComponent.render()
                                .lines()
                                .map(s -> "    " + s + "\n")
                                .collect(Collectors.joining())
                   +
                   """
                   </swap>
                   """;
        }
    }

    public interface HtmlComponent {
        String render();
    }
}
