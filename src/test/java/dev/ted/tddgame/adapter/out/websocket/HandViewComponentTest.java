package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.div;
import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.img;
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

        HtmlElement htmlElement = new HandViewComponent(player).handDiv();

        assertThat(htmlElement)
                .isEqualTo(
                        div("hand",
                            div("card", img("/static/predict.png", "Predict")),
                            div("card", img("/static/cant-assert.png", "Can't Assert")),
                            div("card", img("/static/less-code.png", "Less Code")),
                            div("card", img("/static/write-code.png", "Write Code")),
                            div("card", img("/static/refactor.png", "Refactor"))
                        )
                );
    }

}