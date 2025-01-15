package dev.ted.tddgame.adapter.out.websocket;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("HtmlUnknownTarget")
class HtmlElementTest {

    @Test
    void textComponentIsRenderedBare() {
        HtmlElement textComponent = HtmlElement.text("text component contents");

        assertThat(textComponent.render())
                .isEqualTo("text component contents");
    }

    @Test
    void emptyDivComponentHasOnlyOpenAndCloseTagWithCssClass() {
        HtmlElement div = HtmlElement.div("cssClass");

        assertThat(div.render())
                .isEqualTo("""
                           <div class="cssClass">
                           </div>
                           """);
    }

    @Test
    void emptyDivComponentWithClassAndIdHasOnlyOpenAndCloseTagWithIdAndClass() {
        HtmlElement div = HtmlElement.div("htmlId", "cssClass");

        assertThat(div.render())
                .isEqualTo("""
                           <div id="htmlId" class="cssClass">
                           </div>
                           """);
    }

    @Test
    void divWithNestedTextComponentRendersTextAsIndentedOneLevel() {
        HtmlElement textComponent = HtmlElement.text("nested text");

        HtmlElement div = HtmlElement.div("cssClass", textComponent);

        assertThat(div.render())
                .isEqualTo("""
                           <div class="cssClass">
                               nested text
                           </div>
                           """);
    }

    @Test
    void divWithNestedDivRendersDivAsIndentedOneLevel() {
        HtmlElement div = HtmlElement.div("class of parent",
                                          HtmlElement.div("class of nested"));
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
        HtmlElement div = HtmlElement.div(
                "class of parent",
                HtmlElement.div("class of first nested"),
                HtmlElement.div("class of second nested")
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
        HtmlElement swap = HtmlElement.swapInnerHtml("targetId");

        assertThat(swap.render())
                .isEqualTo("""
                           <swap id="targetId" hx-swap-oob="innerHTML">
                           </swap>
                           """);
    }

    @Test
    void swapWithTextAndDivComponentRendersCorrectly() {
        HtmlElement swap = HtmlElement.swapAfterBegin("swapId", HtmlElement.text("Heading for div"),
                                                      HtmlElement.div("class of second nested",
                                                                      HtmlElement.text("Inside DIV")));

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
        HtmlElement deleteSwap = HtmlElement.swapDelete("deletionTarget");

        assertThat(deleteSwap.render())
                .isEqualTo("""
                           <swap id="deletionTarget" hx-swap-oob="delete">
                           </swap>
                           """);
    }

    @Test
    void imgElementHasSrcAttribute() {
        HtmlElement img = HtmlElement.img("image-name.png", "alt text");

        assertThat(img.render())
                .isEqualTo("""
                           <img src="image-name.png" alt="alt text">
                           """);
    }

    @Test
    void htmxButtonHasAllSpecifiedAttributes() {
        List<HtmlElement.HtmlAttribute> attributes = List.of(
                new HtmlElement.HtmlAttribute("hx-get", "/game/testy-spider-83/card-menu/CANT_ASSERT")
                , new HtmlElement.HtmlAttribute("hx-on::after-settle", "document.querySelector('dialog').showModal()")
                , new HtmlElement.HtmlAttribute("hx-swap", "none")
                , new HtmlElement.HtmlAttribute("class", "card")
        );
        HtmlElement img = HtmlElement.img("/cant-assert.png", "Can't Assert");
        HtmlElement button = HtmlElement.button(attributes, img);

        assertThat(button.render())
                .isEqualTo("""
                           <button hx-get="/game/testy-spider-83/card-menu/CANT_ASSERT" hx-on::after-settle="document.querySelector('dialog').showModal()" hx-swap="none" class="card">
                               <img src="/cant-assert.png" alt="Can't Assert">
                           </button>
                           """);
    }

    @Nested
    class EqualsVerification {
        @Test
        void swapEqualsWithoutNestedComponents() {
            assertThat(HtmlElement.swapDelete("delete-me"))
                    .isEqualTo(HtmlElement.swapDelete("delete-me"));

            assertThat(HtmlElement.swapDelete("red"))
                    .isNotEqualTo(HtmlElement.swapDelete("blue"));
        }

        @Test
        void textComponentEquals() {
            assertThat(HtmlElement.text("text contents"))
                    .as("Text contents are the same, so should be Equal")
                    .isEqualTo(HtmlElement.text("text contents"));

            assertThat(HtmlElement.text("text contents"))
                    .as("Text contents are DIFFERENT, so should NOT be equal")
                    .isNotEqualTo(HtmlElement.text("different text"));
        }

        @Test
        void swapEqualsWithNestedComponentsOneLevel() {
            assertThat(HtmlElement.swapInnerHtml("text-swap", HtmlElement.text("Text inside Swap")))
                    .isEqualTo(HtmlElement.swapInnerHtml("text-swap", HtmlElement.text("Text inside Swap")));

            assertThat(HtmlElement.swapInnerHtml("text-swap", HtmlElement.text("Text inside Swap")))
                    .as("Different targetId, but same nested component contents")
                    .isNotEqualTo(HtmlElement.swapInnerHtml("differentTargetId", HtmlElement.text("Text inside Swap")));

            assertThat(HtmlElement.swapInnerHtml("text-swap", HtmlElement.text("Text inside Swap")))
                    .as("Same targetId, but different nested component contents")
                    .isNotEqualTo(HtmlElement.swapInnerHtml("text-swap", HtmlElement.text("Different Text inside")));
        }

        @Test
        void divEqualsWithoutNestedComponents() {
            assertThat(HtmlElement.div("html-class"))
                    .isEqualTo(HtmlElement.div("html-class"));

            assertThat(HtmlElement.div("html-class"))
                    .isNotEqualTo(HtmlElement.div("different-html-class"));
        }

        @Test
        void divEqualsWithTwoLevelsOfChildComponents() {
            assertThat(HtmlElement.div("has-two-children",
                                       HtmlElement.div("first-child",
                                                       HtmlElement.text("Text of Leaf component"))))
                    .isEqualTo(HtmlElement.div("has-two-children",
                                               HtmlElement.div("first-child",
                                                               HtmlElement.text("Text of Leaf component"))));

            assertThat(HtmlElement.div("has-two-children",
                                       HtmlElement.div("first-child",
                                                       HtmlElement.text("Text of Leaf component"))))
                    .isNotEqualTo(HtmlElement.div("has-two-children",
                                                  HtmlElement.div("first-child",
                                                                  HtmlElement.text("Leaf Text component text is different"))));

            assertThat(HtmlElement.div("has-two-children",
                                       HtmlElement.div("first-child",
                                                       HtmlElement.text("Text of Leaf component"))))
                    .isNotEqualTo(HtmlElement.div("has-two-children",
                                                  HtmlElement.div("different-first-child",
                                                                  HtmlElement.text("Text of Leaf component"))));
        }

    }
}