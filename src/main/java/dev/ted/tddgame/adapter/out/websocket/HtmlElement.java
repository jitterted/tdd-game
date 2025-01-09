package dev.ted.tddgame.adapter.out.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

// https://html.spec.whatwg.org/multipage/infrastructure.html#html-elements
public abstract class HtmlElement {
    protected final List<HtmlElement> childComponents;
    protected final List<HtmlAttribute> attributes = new ArrayList<>();
    protected final String tag;

    public HtmlElement(String tag, HtmlElement... childComponents) {
        Objects.requireNonNull(tag);

        this.childComponents = List.of(childComponents);
        this.tag = tag;
    }

    static Text text(String textComponentContents) {
        return new Text(textComponentContents);
    }

    static HtmlElement div(String cssClass, HtmlElement... childComponents) {
        return new NormalElement("div", null, cssClass, childComponents);
    }

    static HtmlElement div(String htmlId, String cssClass, HtmlElement... childComponents) {
        return new NormalElement("div", htmlId, cssClass, childComponents);
    }

    static HtmlElement swapInnerHtml(String targetId, HtmlElement... childComponents) {
        return new Swap(targetId, "innerHTML", childComponents);
    }

    static HtmlElement swapAfterBegin(String targetId, HtmlElement... childComponents) {
        return new Swap(targetId, "afterbegin", childComponents);
    }

    static HtmlElement swapDelete(String targetId) {
        return new Swap(targetId, "delete");
    }

    static HtmlElement img(String src, String altText) {
        return new ImgElement("img", src, altText);
    }

    protected String renderNested() {
        return childComponents.stream()
                              .map(this::render)
                              .collect(Collectors.joining());
    }

    private String render(HtmlElement component) {
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

    protected String renderAttributes() {
        return attributes.stream()
                         .map(HtmlAttribute::render)
                         .collect(Collectors.joining(" "));
    }

    protected abstract String renderTagClose();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HtmlElement that = (HtmlElement) o;
        return childComponents.equals(that.childComponents) && attributes.equals(that.attributes) && tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        int result = childComponents.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }

    private static final class Swap extends HtmlElement {

        private final String targetId;
        private final String swapStrategy;

        Swap(String targetId, String swapStrategy, HtmlElement... childComponents) {
            super("", childComponents);
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

    static final class Text extends HtmlElement {

        private final String text;

        Text(String text) {
            super("");
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

    // container of components that doesn't render itself
    static final class Forest extends HtmlElement {

        public Forest(HtmlElement... childComponents) {
            super("forest", childComponents);
        }

        @Override
        protected String renderTagOpen() {
            return null;
        }

        @Override
        protected String renderTagClose() {
            return null;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Forest.class.getSimpleName() + "[", "]")
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

    // https://html.spec.whatwg.org/multipage/syntax.html#normal-elements
    static class NormalElement extends HtmlElement {

        NormalElement(String tag, String htmlId, String cssClass, HtmlElement... childComponents) {
            super(tag, childComponents);
            if (htmlId != null) {
                attributes.add(new HtmlAttribute("id", htmlId));
            }
            if (cssClass != null) {
                attributes.add(new HtmlAttribute("class", cssClass));
            }
        }

        @Override
        protected String renderTagOpen() {
            return """
                   <%s %s>
                   """.formatted(tag, renderAttributes());
        }

        @Override
        protected String renderTagClose() {
            return "</" + tag + ">\n";
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }

            NormalElement that = (NormalElement) o;
            return attributes.equals(that.attributes) && tag.equals(that.tag);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + attributes.hashCode();
            result = 31 * result + tag.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                    .add("tag=" + tag)
                    .add("attributes=" + attributes)
                    .add("childComponents=" + childComponents)
                    .toString();
        }
    }

    // could become VoidElement: https://html.spec.whatwg.org/multipage/syntax.html#void-elements
    static class ImgElement extends HtmlElement {
        public ImgElement(String tag, String src, String altText) {
            super(tag);
            Objects.requireNonNull(src);
            Objects.requireNonNull(altText);
            attributes.add(new HtmlAttribute("src", src));
            attributes.add(new HtmlAttribute("alt", altText));
        }

        @Override
        protected String renderTagOpen() {
            return """
                   <%s %s>
                   """.formatted(tag, renderAttributes());
        }

        @Override
        protected String renderTagClose() {
            return "";
        }


        @Override
        public String toString() {
            return new StringJoiner(", ", ImgElement.class.getSimpleName() + "[", "]")
                    .add("tag='" + tag + "'")
                    .add("attributes=" + attributes)
                    .toString();
        }
    }
}
