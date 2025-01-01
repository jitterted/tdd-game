package dev.ted.tddgame.adapter.out.websocket;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HtmlComponentTest {

    @Test
    void textComponentIsRenderedBare() {
        HtmlComponent textComponent = HtmlComponent.text("text component contents");

        assertThat(textComponent.render())
                .isEqualTo("text component contents");
    }

    @Test
    void emptyDivComponentHasOnlyOpenAndCloseTagWithCssClass() {
        HtmlComponent div = HtmlComponent.div("cssClass");

        assertThat(div.render())
                .isEqualTo("""
                           <div class="cssClass">
                           </div>
                           """);
    }

    @Test
    void divWithNestedTextComponentRendersTextAsIndentedOneLevel() {
        HtmlComponent textComponent = HtmlComponent.text("nested text");

        HtmlComponent div = HtmlComponent.div("cssClass", textComponent);

        assertThat(div.render())
                .isEqualTo("""
                           <div class="cssClass">
                               nested text
                           </div>
                           """);
    }

    @Test
    void divWithNestedDivRendersDivAsIndentedOneLevel() {
        HtmlComponent div = HtmlComponent.div("class of parent",
                                              HtmlComponent.div("class of nested"));
        assertThat(div.render())
                .isEqualTo("""
                           <div class="class of parent">
                               <div class="class of nested">
                               </div>
                           </div>
                           """);
    }
}