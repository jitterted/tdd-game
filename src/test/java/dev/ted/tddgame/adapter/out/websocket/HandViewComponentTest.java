package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerDrewActionCard;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.div;
import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.text;
import static org.assertj.core.api.Assertions.*;

class HandViewComponentTest {

    private static final PlayerId IRRELEVANT_PLAYER_ID = new PlayerId(4L);
    private static final MemberId IRRELEVANT_MEMBER_ID = new MemberId(42L);

    @Test
    void htmlForPlayerWithFiveCardsGeneratesCorrectDiv() {
        Player player = new Player(IRRELEVANT_PLAYER_ID, IRRELEVANT_MEMBER_ID, "IRRELEVANT PLAYER NAME", _ -> {});
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.PREDICT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.PREDICT));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.LESS_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.WRITE_CODE));
        player.apply(new PlayerDrewActionCard(player.memberId(), ActionCard.REFACTOR));

        HtmlElement htmlElement = new HandViewComponent(player).handDiv();

        assertThat(htmlElement)
                .isEqualTo(
                        div("hand",
                            div("card", text("predict")),
                            div("card", text("predict")),
                            div("card", text("less code")),
                            div("card", text("write code")),
                            div("card", text("refactor"))
                        )
                );
    }

}