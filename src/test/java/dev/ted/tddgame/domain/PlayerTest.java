package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerTest {

    private static final long IRRELEVANT_PLAYER_ID = 1L;
    private static final long IRRELEVANT_MEMBER_ID = 213L;

    @Test
    void newPlayerHasEmptyHand() {
        Player player = createPlayer();

        assertThat(player.hand())
                .isEmpty();
    }

    @Test
    void playerHasTwoActionCardsInHandAfterDrawingActionCardTwice() {
        Player player = createPlayer();

        player.addCardToHand(ActionCard.LESS_CODE);
        player.addCardToHand(ActionCard.WRITE_CODE);

        assertThat(player.hand())
                .containsExactly(ActionCard.LESS_CODE, ActionCard.WRITE_CODE);
    }


    private Player createPlayer() {
        return new Player(new PlayerId(IRRELEVANT_PLAYER_ID),
                          new MemberId(IRRELEVANT_MEMBER_ID),
                          "Player 1");
    }
}