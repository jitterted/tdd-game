package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerViewComponent {
    private final Player player;

    public PlayerViewComponent(Player player) {
        this.player = player;
    }

    public String generateHtmlAsYou() {
        AbstractHtmlComponent divComponent = new DivHtmlComponent("workspace", new TextComponent("<h2>Workspace</h2>"));
        String html = divComponent.render()
                      +
                      """
                      <div class="titled-container">
                          Your Hand
                          %s
                      </div>
                      """.formatted(new HandComponent(player.hand()).render());
        return new SwapComponent(() -> html).render();
    }

    static class HandComponent extends AbstractHtmlComponent {
        private final Stream<ActionCard> actionCards;

        public HandComponent(Stream<ActionCard> actionCards) {
            this.actionCards = actionCards;
        }

        @Override
        public String render() {
            return """
                   <div class="hand">
                   """
                   +
                   actionCards
                           .map(card -> """
                                                <div class="card">%s</div>
                                        """
                                   .formatted(card.title()))
                           .collect(Collectors.joining())
                   +
                   "    </div>";
        }
    }

    static class SwapComponent extends AbstractHtmlComponent {

        public SwapComponent(HtmlComponent htmlComponent) {
            super(htmlComponent);
        }

        @Override
        public String render() {
            return """
                   <swap id="you" hx-swap-oob="innerHTML">
                   """
                   +
                   renderNested()
                   +
                   """
                   </swap>
                   """;
        }
    }

    public interface HtmlComponent {
        String render();
    }

    public abstract static class AbstractHtmlComponent implements HtmlComponent {
        protected final List<HtmlComponent> htmlComponent;

        public AbstractHtmlComponent(HtmlComponent... htmlComponent) {
            this.htmlComponent = List.of(htmlComponent);
        }

        protected String renderNested() {
            return htmlComponent.stream()
                                .map(this::render)
                                .collect(Collectors.joining());
        }

        private String render(HtmlComponent component) {
            return component.render()
                            .lines()
                            .map(s -> "    " + s + "\n")
                            .collect(Collectors.joining());
        }
    }

    private static class DivHtmlComponent extends AbstractHtmlComponent {

        private final String htmlClass;

        public DivHtmlComponent(String htmlClass, HtmlComponent... htmlComponents) {
            super(htmlComponents);
            this.htmlClass = htmlClass;
        }

        @Override
        public String render() {
            return """
                   <div class="%s">
                   """.formatted(htmlClass)
                   +
                   renderNested()
                   +
                   """
                   </div>
                   """;
        }

    }

    private static class TextComponent implements HtmlComponent {

        private final String text;

        public TextComponent(String text) {
            this.text = text;
        }

        @Override
        public String render() {
            return text;
        }
    }
}
