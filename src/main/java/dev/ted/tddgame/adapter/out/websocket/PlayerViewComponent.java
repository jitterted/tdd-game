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
        HtmlComponent handComponent = new DivHtmlComponent("hand",
                                                           createDivsForEach(player.hand()));
        HtmlComponent handContainerDiv = new DivHtmlComponent("titled-container",
                                                              new TextComponent("Your Hand"),
                                                              handComponent);
        return new SwapComponent(workspaceDiv, handContainerDiv)
                .render();
    }

    static HtmlComponent[] createDivsForEach(Stream<ActionCard> actionCards) {
        return actionCards
                .map(card -> new DivHtmlComponent("card",
                                                  new TextComponent(card.title())))
                .toList()
                .toArray(new HtmlComponent[0]);
    }

    static class SwapComponent extends HtmlComponent {

        public SwapComponent(HtmlComponent... htmlComponent) {
            super(htmlComponent);
        }

        @Override
        protected String renderTagOpen() {
            return """
                   <swap id="you" hx-swap-oob="innerHTML">
                   """;
        }

        @Override
        protected String renderTagClose() {
            return "</swap>\n";
        }
    }

    private static class DivHtmlComponent extends HtmlComponent {

        private final String htmlClass;

        public DivHtmlComponent(String htmlClass, HtmlComponent... htmlComponents) {
            super(htmlComponents);
            this.htmlClass = htmlClass;
        }

        @Override
        protected String renderTagOpen() {
            return """
                   <div class="%s">
                   """.formatted(htmlClass);
        }

        @Override
        protected String renderTagClose() {
            return "</div>";
        }

    }

    private static class TextComponent extends HtmlComponent {

        private final String text;

        public TextComponent(String text) {
            this.text = text;
        }

        public String render() {
            return text;
        }

        @Override
        protected String renderTagClose() {
            return "";
        }

        @Override
        protected String renderTagOpen() {
            return "";
        }
    }

    public abstract static class HtmlComponent {
        protected final List<HtmlComponent> htmlComponent;

        public HtmlComponent(HtmlComponent... htmlComponent) {
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

        public String render() {
            return renderTagOpen()
                   +
                   renderNested()
                   +
                   renderTagClose();
        }

        protected abstract String renderTagClose();

        protected abstract String renderTagOpen();
    }

}
