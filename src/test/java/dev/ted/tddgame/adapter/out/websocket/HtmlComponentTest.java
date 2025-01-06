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

    @Test
    void divWithTwoNestedDivsRendersBothNestedAtSameLevelInOrder() {
        HtmlComponent div = HtmlComponent.div(
                "class of parent",
                HtmlComponent.div("class of first nested"),
                HtmlComponent.div("class of second nested")
        );

        assertThat(div.render())
                .isEqualTo("""
                           <div class="class of parent">
                               <div class="class of first nested">
                               </div>
                               <div class="class of second nested">
                               </div>
                           </div>
                           """);
    }

    @Test
    void emptySwapInnerHtmlRendersWithIdAndStrategy() {
        HtmlComponent swap = HtmlComponent.swapInnerHtml("targetId");

        assertThat(swap.render())
                .isEqualTo("""
                           <swap id="targetId" hx-swap-oob="innerHTML">
                           </swap>
                           """);
    }

    @Test
    void swapWithTextAndDivComponentRendersCorrectly() {
        HtmlComponent swap = HtmlComponent.swapAfterBegin("swapId", HtmlComponent.text("Heading for div"),
                                                          HtmlComponent.div("class of second nested",
                                                                            HtmlComponent.text("Inside DIV")));

        assertThat(swap.render())
                .isEqualTo("""
                           <swap id="swapId" hx-swap-oob="afterbegin">
                               Heading for div
                               <div class="class of second nested">
                                   Inside DIV
                               </div>
                           </swap>
                           """);
    }

    @Test
    void deletingSwapRenderedAsEmptySwapWithDeleteStrategy() {
        HtmlComponent deleteSwap = HtmlComponent.swapDelete("deletionTarget");

        assertThat(deleteSwap.render())
                .isEqualTo("""
                           <swap id="deletionTarget" hx-swap-oob="delete">
                           </swap>
                           """);
    }

    @Test
    void swapEqualsWithoutNestedComponentsTest() {
        assertThat(HtmlComponent.swapDelete("delete-me"))
                .isEqualTo(HtmlComponent.swapDelete("delete-me"));

        assertThat(HtmlComponent.swapDelete("red"))
                .isNotEqualTo(HtmlComponent.swapDelete("blue"));
    }

    @Test
    void textComponentEqualsTest() {
        assertThat(HtmlComponent.text("text contents"))
                .as("Text contents are the same, so should be Equal")
                .isEqualTo(HtmlComponent.text("text contents"));

        assertThat(HtmlComponent.text("text contents"))
                .as("Text contents are DIFFERENT, so should NOT be equal")
                .isNotEqualTo(HtmlComponent.text("different text"));
    }

    @Test
    void swapEqualsWithNestedComponentsTest() {
        assertThat(HtmlComponent.swapInnerHtml("text-swap", HtmlComponent.text("Text inside Swap")))
                .isEqualTo(HtmlComponent.swapInnerHtml("text-swap", HtmlComponent.text("Text inside Swap")));

        assertThat(HtmlComponent.swapInnerHtml("text-swap", HtmlComponent.text("Text inside Swap")))
                .as("Different targetId, but same nested component contents")
                .isEqualTo(HtmlComponent.swapInnerHtml("differentTargetId", HtmlComponent.text("Text inside Swap")));

        assertThat(HtmlComponent.swapInnerHtml("text-swap", HtmlComponent.text("Text inside Swap")))
                .as("Same targetId, but different nested component contents")
                .isNotEqualTo(HtmlComponent.swapInnerHtml("text-swap", HtmlComponent.text("Different Text inside")));
    }
}