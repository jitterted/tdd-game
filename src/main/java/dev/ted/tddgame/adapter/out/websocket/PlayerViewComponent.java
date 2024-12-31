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
        HtmlComponent workspaceDiv = new DivHtmlComponent("workspace",
                                                          new TextComponent("<h2>Workspace</h2>"));
        HtmlComponent handContainerDiv = new DivHtmlComponent("titled-container",
                                                              new TextComponent("Your Hand"),
                                                              new HandComponent(player.hand()));
        return new SwapComponent(workspaceDiv, handContainerDiv)
                .render();
    }

    static class HandComponent extends AbstractHtmlComponent {

        public HandComponent(Stream<ActionCard> actionCards) {
            super(convert(actionCards));
        }

        private static HtmlComponent[] convert(Stream<ActionCard> actionCards) {
            return actionCards
                    .map(card -> new DivHtmlComponent("card",
                                                      new TextComponent(card.title())))
                    .toList()
                    .toArray(new HtmlComponent[0]);
        }

        @Override
        public String render() {
            return """
                   <div class="hand">
                   """
                   +
                   renderNested()
                   +
                   "</div>";
        }
    }

    static class SwapComponent extends AbstractHtmlComponent {

        public SwapComponent(HtmlComponent... htmlComponent) {
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
