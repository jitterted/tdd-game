package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.ted.tddgame.adapter.HtmlElement.div;
import static dev.ted.tddgame.adapter.HtmlElement.faIcon;
import static dev.ted.tddgame.adapter.HtmlElement.forest;
import static dev.ted.tddgame.adapter.HtmlElement.swapBeforeEnd;
import static dev.ted.tddgame.adapter.HtmlElement.swapDelete;
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
        HtmlElement div = div("cssClass");

        assertThat(div.render())
                .isEqualTo("""
                           <div class="cssClass">
                           </div>
                           """);
    }

    @Test
    void emptyDivComponentWithClassAndIdHasOnlyOpenAndCloseTagWithIdAndClass() {
        HtmlElement div = div("htmlId", "cssClass");

        assertThat(div.render())
                .isEqualTo("""
                           <div id="htmlId" class="cssClass">
                           </div>
                           """);
    }

    @Test
    void divWithNestedTextComponentRendersTextAsIndentedOneLevel() {
        HtmlElement textComponent = HtmlElement.text("nested text");

        HtmlElement div = div("cssClass", textComponent);

        assertThat(div.render())
                .isEqualTo("""
                           <div class="cssClass">
                               nested text
                           </div>
                           """);
    }

    @Test
    void divWithNestedDivRendersDivAsIndentedOneLevel() {
        HtmlElement div = div("class of parent",
                              div("class of nested"));
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
        HtmlElement div = div(
                "class of parent",
                div("class of first nested"),
                div("class of second nested")
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
                                                      div("class of second nested",
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

    @Test
    void builderGeneratesSameHtmlAsNonBuilderMethods() {
        String builderRenderedHtml =
                forest()
                        .addChildren(
                                swapDelete("workspace1-pawn"),
                                swapBeforeEnd("what-should-it-do-hex-tile")
                                        .addChildren(
                                                div()
                                                        .id("workspace1-pawn")
                                                        .classNames("hex-tile-stack-pawn")
                                                        .addChildren(
                                                                faIcon("fa-regular fa-circle-1")
                                                        )
                                        )
                        ).render();

        String parameterRenderedHtml =
                forest(
                        swapDelete("workspace1-pawn"),
                        swapBeforeEnd("what-should-it-do-hex-tile",
                                      div("workspace1-pawn", "hex-tile-stack-pawn",
                                          faIcon("fa-regular fa-circle-1")
                                      )
                        ))
                        .render();

        assertThat(builderRenderedHtml)
                .isEqualTo(parameterRenderedHtml);
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
            assertThat(div("html-class"))
                    .isEqualTo(div("html-class"));

            assertThat(div("html-class"))
                    .isNotEqualTo(div("different-html-class"));
        }

        @Test
        void divEqualsWithTwoLevelsOfChildComponents() {
            assertThat(div("has-two-children",
                           div("first-child",
                               HtmlElement.text("Text of Leaf component"))))
                    .isEqualTo(div("has-two-children",
                                   div("first-child",
                                       HtmlElement.text("Text of Leaf component"))));

            assertThat(div("has-two-children",
                           div("first-child",
                               HtmlElement.text("Text of Leaf component"))))
                    .isNotEqualTo(div("has-two-children",
                                      div("first-child",
                                          HtmlElement.text("Leaf Text component text is different"))));

            assertThat(div("has-two-children",
                           div("first-child",
                               HtmlElement.text("Text of Leaf component"))))
                    .isNotEqualTo(div("has-two-children",
                                      div("different-first-child",
                                          HtmlElement.text("Text of Leaf component"))));
        }

    }
}