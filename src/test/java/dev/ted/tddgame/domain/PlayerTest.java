package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerTest {

    private static final long IRRELEVANT_PLAYER_ID = 1L;
    private static final long IRRELEVANT_MEMBER_ID = 213L;

    @Test
    void newPlayerHasEmptyHand() {
        Player player = createPlayer(IRRELEVANT_MEMBER_ID);

        assertThat(player.hand())
                .isEmpty();
    }

    @Nested
    class EventsProjectState {

        @Test
        void drewActionCardTwiceResultsInTwoCardsInHand() {
            MemberId memberId = new MemberId(37L);
            Player player = createPlayer(memberId.id());
            List<PlayerEvent> events = List.of(
                    new PlayerDrewActionCard(memberId, ActionCard.LESS_CODE),
                    new PlayerDrewActionCard(memberId, ActionCard.WRITE_CODE));

            events.forEach(player::apply);

            assertThat(player.hand())
                    .containsExactly(ActionCard.LESS_CODE, ActionCard.WRITE_CODE);
        }

        @Test
        void playerHasTwoActionCardsInHandAfterDrawingSameTypeOfActionCardTwice() {
            MemberId memberId = new MemberId(37L);
            Player player = createPlayer(memberId.id());
            List<PlayerEvent> events = List.of(
                    new PlayerDrewActionCard(memberId, ActionCard.PREDICT),
                    new PlayerDrewActionCard(memberId, ActionCard.PREDICT));

            events.forEach(player::apply);

            assertThat(player.hand())
                    .hasSize(2)
                    .containsOnly(ActionCard.PREDICT);
        }

    }


    private static Player createPlayer(long memberId) {
        final PlayerId playerId = new PlayerId(IRRELEVANT_PLAYER_ID);
        return new Player(playerId,
                          new MemberId(memberId),
                          "Player 1",
                          null, new Workspace(playerId));
    }
}