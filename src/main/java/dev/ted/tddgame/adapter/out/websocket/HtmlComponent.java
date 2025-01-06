package dev.ted.tddgame.adapter.out.websocket;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public abstract class HtmlComponent {
    protected final List<HtmlComponent> childComponents;

    public HtmlComponent(HtmlComponent... childComponents) {
        this.childComponents = List.of(childComponents);
    }

    static Text text(String textComponentContents) {
        return new Text(textComponentContents);
    }

    static Div div(String cssClass, HtmlComponent... childComponents) {
        return new Div(cssClass, childComponents);
    }

    static Div div(String htmlId, String cssClass, HtmlComponent... childComponents) {
        return new Div(htmlId, cssClass, childComponents);
    }

    static Swap swapInnerHtml(String targetId, HtmlComponent... childComponents) {
        return new Swap(targetId, "innerHTML", childComponents);
    }

    static Swap swapAfterBegin(String targetId, HtmlComponent... childComponents) {
        return new Swap(targetId, "afterbegin", childComponents);
    }

    static Swap swapDelete(String targetId) {
        return new Swap(targetId, "delete");
    }

    protected String renderNested() {
        return childComponents.stream()
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
        return childComponents.equals(that.childComponents);
    }

    @Override
    public int hashCode() {
        return childComponents.hashCode();
    }

    static final class Swap extends HtmlComponent {

        private final String targetId;
        private final String swapStrategy;

        Swap(String targetId, String swapStrategy, HtmlComponent... childComponents) {
            super(childComponents);
            Objects.requireNonNull(targetId);
            Objects.requireNonNull(swapStrategy);
            Objects.requireNonNull(childComponents);
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
                    .add("childComponents=" + childComponents)
                    .add("swapStrategy='" + swapStrategy + "'")
                    .add("targetId='" + targetId + "'")
                    .toString();
        }
    }

    static class Div extends HtmlComponent {

        private final String cssClass;
        private final String htmlId;

        Div(String cssClass, HtmlComponent... childComponents) {
            this(null, cssClass, childComponents);
        }

        public Div(String htmlId, String cssClass, HtmlComponent... childComponents) {
            super(childComponents);
            Objects.requireNonNull(cssClass);
//            Objects.requireNonNull(htmlId);
            this.cssClass = cssClass;
            this.htmlId = htmlId;
        }

        @Override
        protected String renderTagOpen() {
            String attributes = "";
            if (htmlId != null) {
                attributes += "id=\"" + htmlId + "\" ";
            }
            attributes += "class=\"" + cssClass + "\"";
            return """
                   <div %s>
                   """.formatted(attributes);
        }

        @Override
        protected String renderTagClose() {
            return "</div>\n";
        }

        @Override
        public final boolean equals(Object o) {
            if (!(o instanceof Div div)) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }

            return cssClass.equals(div.cssClass);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + cssClass.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Div.class.getSimpleName() + "[", "]")
                    .add("htmlClass='" + cssClass + "'")
                    .add("Nested HTML Components=" + childComponents)
                    .toString();
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
            if (!super.equals(o)) {
                return false;
            }

            return text.equals(text1.text);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + text.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Text.class.getSimpleName() + "[", "]")
                    .add("text='" + text + "'")
                    .toString();
        }
    }
}
