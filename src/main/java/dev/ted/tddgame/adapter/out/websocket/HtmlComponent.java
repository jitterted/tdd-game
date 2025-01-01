package dev.ted.tddgame.adapter.out.websocket;

import java.util.List;
import java.util.stream.Collectors;

public abstract class HtmlComponent {
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

    static class Div extends HtmlComponent {

        private final String htmlClass;

        public Div(String htmlClass, HtmlComponent... htmlComponents) {
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

    static class Text extends HtmlComponent {

        private final String text;

        public Text(String text) {
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
}
