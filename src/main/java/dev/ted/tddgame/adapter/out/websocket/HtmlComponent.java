package dev.ted.tddgame.adapter.out.websocket;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public abstract class HtmlComponent {
    protected final List<HtmlComponent> htmlComponents;

    public HtmlComponent(HtmlComponent... htmlComponents) {
        this.htmlComponents = List.of(htmlComponents);
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
        return htmlComponents.stream()
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HtmlComponent that = (HtmlComponent) o;
        return htmlComponents.equals(that.htmlComponents);
    }

    @Override
    public int hashCode() {
        return htmlComponents.hashCode();
    }

    static final class Swap extends HtmlComponent {

        private final String targetId;
        private final String swapStrategy;

        Swap(String targetId, String swapStrategy, HtmlComponent... htmlComponents) {
            super(htmlComponents);
            Objects.requireNonNull(targetId);
            Objects.requireNonNull(swapStrategy);
            Objects.requireNonNull(htmlComponents);
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

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Swap swap)) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }

            return targetId.equals(swap.targetId) && swapStrategy.equals(swap.swapStrategy);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + targetId.hashCode();
            result = 31 * result + swapStrategy.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Swap.class.getSimpleName() + "[", "]")
                    .add("htmlComponents=" + htmlComponents)
                    .add("swapStrategy='" + swapStrategy + "'")
                    .add("targetId='" + targetId + "'")
                    .toString();
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

    static final class Text extends HtmlComponent {

        private final String text;

        Text(String text) {
            Objects.requireNonNull(text);
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

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Text text1)) {
                return false;
            }

            return text.equals(text1.text);
        }

        @Override
        public int hashCode() {
            return text.hashCode();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Text.class.getSimpleName() + "[", "]")
                    .add("text='" + text + "'")
                    .toString();
        }
    }
}
