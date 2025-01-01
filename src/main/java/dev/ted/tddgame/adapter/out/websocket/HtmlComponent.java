package dev.ted.tddgame.adapter.out.websocket;

import java.util.List;
import java.util.stream.Collectors;

public abstract class HtmlComponent {
    protected final List<HtmlComponent> htmlComponent;

    public HtmlComponent(HtmlComponent... htmlComponent) {
        this.htmlComponent = List.of(htmlComponent);
    }

    static Text text(String textComponentContents) {
        return new Text(textComponentContents);
    }

    static Div div(String cssClass, HtmlComponent... htmlComponents) {
        return new Div(cssClass, htmlComponents);
    }

    static Swap swapInnerHtml(String targetId, HtmlComponent... htmlComponents) {
        return new Swap(targetId, "innerHTML", htmlComponents);
    }

    static Swap swapAfterBegin(String targetId, HtmlComponent... htmlComponents) {
        return new Swap(targetId, "afterbegin", htmlComponents);
    }

    static Swap swapDelete(String targetId) {
        return new Swap(targetId, "delete");
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

    String render() {
        return renderTagOpen()
               +
               renderNested()
               +
               renderTagClose();
    }

    protected abstract String renderTagOpen();

    protected abstract String renderTagClose();

    static class Swap extends HtmlComponent {

        private final String targetId;
        private final String swapStrategy;

        Swap(String targetId, String swapStrategy, HtmlComponent... htmlComponents) {
            super(htmlComponents);
            this.targetId = targetId;
            this.swapStrategy = swapStrategy;
        }

        @Override
        protected String renderTagOpen() {
            return """
                   <swap id="%s" hx-swap-oob="%s">
                   """.formatted(targetId, swapStrategy);
        }

        @Override
        protected String renderTagClose() {
            return "</swap>\n";
        }
    }

    static class Div extends HtmlComponent {

        private final String htmlClass;

        Div(String htmlClass, HtmlComponent... htmlComponents) {
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
            return "</div>\n";
        }

    }

    static class Text extends HtmlComponent {

        private final String text;

        Text(String text) {
            this.text = text;
        }

        @Override
        protected String renderNested() {
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
