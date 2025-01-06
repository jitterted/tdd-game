package dev.ted.tddgame.adapter.out.websocket;

import java.util.ArrayList;
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

        private final List<HtmlAttribute> attributes = new ArrayList<>();

        Div(String cssClass, HtmlComponent... childComponents) {
            this(null, cssClass, childComponents);
        }

        public Div(String htmlId, String cssClass, HtmlComponent... childComponents) {
            super(childComponents);
            Objects.requireNonNull(cssClass);
//            Objects.requireNonNull(htmlId);
            if (htmlId != null) {
                attributes.add(new HtmlAttribute("id", htmlId));
            }
            attributes.add(new HtmlAttribute("class", cssClass));
        }

        @Override
        protected String renderTagOpen() {
            String renderedAttributes = attributes.stream()
                                                  .map(HtmlAttribute::render)
                                                  .collect(Collectors.joining(" "));
            return """
                   <div %s>
                   """.formatted(renderedAttributes);
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

            return attributes.equals(div.attributes);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + attributes.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Div.class.getSimpleName() + "[", "]")
                    .add("attributes=" + attributes)
                    .add("childComponents=" + childComponents)
                    .toString();
        }
    }

    static class HtmlAttribute {
        private final String name;
        private final String value;

        public HtmlAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        String render() {
            if (value == null) {
                return "";
            }
            return name + "=\"" + value + "\"";
        }

        @Override
        public final boolean equals(Object o) {
            if (!(o instanceof HtmlAttribute that)) {
                return false;
            }

            return name.equals(that.name) && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + value.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", HtmlAttribute.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("value='" + value + "'")
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
