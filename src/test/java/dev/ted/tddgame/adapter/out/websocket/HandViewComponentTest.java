package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static dev.ted.tddgame.adapter.HtmlElement.button;
import static dev.ted.tddgame.adapter.HtmlElement.div;
import static dev.ted.tddgame.adapter.HtmlElement.img;
import static org.assertj.core.api.Assertions.*;

class HandViewComponentTest {

    private static final PlayerId IRRELEVANT_PLAYER_ID = new PlayerId(4L);
    private static final MemberId IRRELEVANT_MEMBER_ID = new MemberId(42L);

    @Test
    void htmlForPlayerWithFiveCardsGeneratesCorrectDiv() {
        Player player = new Player(IRRELEVANT_PLAYER_ID, IRRELEVANT_MEMBER_ID, "IRRELEVANT PLAYER NAME", _ -> {});
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.PREDICT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.CANT_ASSERT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.LESS_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.WRITE_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.REFACTOR));

        HtmlElement htmlElement = new HandViewComponent("some-game-handle", player).handContainer();

        assertThat(htmlElement)
                .isEqualTo(
                        div("hand",
                            button(buttonAttributes("/game/some-game-handle/card-menu/PREDICT"), img("/predict.png", "Predict")),
                            button(buttonAttributes("/game/some-game-handle/card-menu/CANT_ASSERT"), img("/cant-assert.png", "Can't Assert")),
                            button(buttonAttributes("/game/some-game-handle/card-menu/LESS_CODE"), img("/less-code.png", "Less Code")),
                            button(buttonAttributes("/game/some-game-handle/card-menu/WRITE_CODE"), img("/write-code.png", "Write Code")),
                            button(buttonAttributes("/game/some-game-handle/card-menu/REFACTOR"), img("/refactor.png", "Refactor"))
                        )
                );
    }

    static HtmlElement.Attributes buttonAttributes(String hxGetUrlPath) {
        return HtmlElement.attributes()
                          .cssClass("card")
                          .hxGet(hxGetUrlPath)
                          .hxSwap("none")
                          .hxOn("after-settle", "document.querySelector('dialog').showModal()");
    }

}