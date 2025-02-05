package dev.ted.tddgame.adapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

// https://html.spec.whatwg.org/multipage/infrastructure.html#html-elements
public abstract class HtmlElement {
    protected final List<HtmlElement> childElements = new ArrayList<>();
    private final List<HtmlAttribute> attributes = new ArrayList<>();
    protected final String tag;

    public HtmlElement(String tag, HtmlElement... childElements) {
        this(tag, Collections.emptyList(), childElements);
    }

    public HtmlElement(String tag,
                       List<HtmlAttribute> htmlAttributes,
                       HtmlElement... childElements) {
        Objects.requireNonNull(tag);

        this.tag = tag;
        Collections.addAll(this.childElements, childElements);
        this.attributes.addAll(htmlAttributes);
    }

    public static HtmlElement forest(HtmlElement... childElements) {
        return new Forest(childElements);
    }

    public static Forest forest() {
        return new Forest();
    }

    public static Text text(String textContents) {
        return new Text(textContents);
    }

    public static HtmlElement div(String cssClass, HtmlElement... childElements) {
        return new NormalElement("div",
                                 HtmlAttribute.of("class", cssClass),
                                 childElements);
    }

    public static HtmlElement div(String htmlId, String cssClass, HtmlElement... childElements) {
        return new NormalElement("div",
                                 HtmlAttribute.of("id", htmlId, "class", cssClass),
                                 childElements);
    }

    public static HtmlElement swapInnerHtml(String targetId, HtmlElement... childElements) {
        return new NormalElement("swap",
                                 HtmlAttribute.of("id", targetId, "hx-swap-oob", "innerHTML"),
                                 childElements);
    }

    public static HtmlElement swapAfterBegin(String targetId, HtmlElement... childElements) {
        return new NormalElement("swap",
                                 HtmlAttribute.of("id", targetId, "hx-swap-oob", "afterbegin"),
                                 childElements);
    }

    public static HtmlElement swapBeforeEnd(String targetId, HtmlElement... childElements) {
        return new NormalElement("swap",
                                 HtmlAttribute.of("id", targetId, "hx-swap-oob", "beforeend"),
                                 childElements);
    }

    public static HtmlElement swapDelete(String targetId) {
        return new NormalElement("swap",
                                 HtmlAttribute.of("id", targetId, "hx-swap-oob", "delete")
        );
    }

    public static HtmlElement img(String src, String altText) {
        return new ImgElement("img", src, altText);
    }

    public static HtmlElement button(List<HtmlAttribute> attributes, HtmlElement... childElements) {
        return new NormalElement("button", attributes, childElements);
    }

    public static HtmlElement button(HtmlAttributes htmlAttributes, HtmlElement... childElements) {
        return button(htmlAttributes.get(), childElements);
    }

    public static HtmlElement faIcon(String classNames) {
        return new NormalElement("i",
                                 attributes().cssClass(classNames)
                                             .style("color: #63E6BE;")
                                             .get());
    }

    public static HtmlAttributes attributes() {
        return new HtmlAttributes();
    }

    public static HtmlElement div() {
        return new NormalElement("div");
    }

    public static HtmlElement h2(String textContents) {
        return new NormalElement("h2")
                .addChildren(text(textContents));
    }

    public HtmlElement addChildren(HtmlElement... childElements) {
        Collections.addAll(this.childElements, childElements);
        return this;
    }

    public HtmlElement id(String htmlId) {
        attributes.add(new HtmlAttribute("id", htmlId));
        return this;
    }

    public HtmlElement classNames(String classNames) {
        attributes.add(new HtmlAttribute("class", classNames));
        return this;
    }


    protected String renderNested() {
        return childElements.stream()
                            .map(this::render)
                            .collect(Collectors.joining());
    }

    private String render(HtmlElement htmlElement) {
        return htmlElement.render()
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
        return childElements.equals(that.childElements)
               && attributes.equals(that.attributes)
               && tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        int result = childElements.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HtmlElement.class.getSimpleName() + "[", "]")
                .add("tag='" + tag + "'")
                .add("attributes=" + attributes)
                .add("childElements=" + childElements)
                .toString();
    }

    public static final class Text extends HtmlElement {

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

    // container of elements that doesn't render itself
    public static final class Forest extends HtmlElement {

        public Forest(HtmlElement... childElements) {
            super("forest", childElements);
        }

        @Override
        protected String renderTagOpen() {
            return "";
        }

        @Override
        protected String renderTagClose() {
            return "";
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Forest.class.getSimpleName() + "[", "]")
                    .add("childElements=" + childElements)
                    .toString();
        }

    }

    public static class HtmlAttribute {
        private final String name;
        private final String value;

        public HtmlAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public static List<HtmlAttribute> of(String name, String value) {
            return List.of(new HtmlAttribute(name, value));
        }

        static List<@NotNull HtmlAttribute> of(String name1, String value1, String name2, String value2) {
            return List.of(new HtmlAttribute(name1, value1), new HtmlAttribute(name2, value2));
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
    public static class NormalElement extends HtmlElement {

        public NormalElement(String tag, List<@NotNull HtmlAttribute> htmlAttributes, HtmlElement... childElements) {
            super(tag, htmlAttributes, childElements);
        }

        public NormalElement(String tag) {
            super(tag);
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

    }

    // could become VoidElement: https://html.spec.whatwg.org/multipage/syntax.html#void-elements
    public static class ImgElement extends HtmlElement {
        public ImgElement(String tag, String src, String altText) {
            super(tag, HtmlAttribute.of("src", src, "alt", altText));
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

    }

    public static class HtmlAttributes {
        private final List<HtmlAttribute> attributes = new ArrayList<>();

        public HtmlAttributes cssClass(String cssClassName) {
            attributes.addAll(HtmlAttribute.of("class", cssClassName));
            return this;
        }

        public HtmlAttributes hxGet(String urlPath) {
            attributes.addAll(HtmlAttribute.of("hx-get", urlPath));
            return this;
        }

        public HtmlAttributes hxSwap(String swapStrategy) {
            attributes.addAll(HtmlAttribute.of("hx-swap", swapStrategy));
            return this;
        }

        public HtmlAttributes hxOn(String htmxEventName, String value) {
            attributes.addAll(HtmlAttribute.of("hx-on::" + htmxEventName, value));
            return this;
        }

        public HtmlAttributes autofocus() {
            attributes.addAll(HtmlAttribute.of("autofocus", ""));
            return this;
        }

        public HtmlAttributes hxPost(String urlPath) {
            attributes.addAll(HtmlAttribute.of("hx-post", urlPath));
            return this;
        }

        public HtmlAttributes style(String styles) {
            attributes.addAll(HtmlAttribute.of("style", styles));
            return this;
        }

        public List<HtmlAttribute> get() {
            return attributes;
        }
    }
}
