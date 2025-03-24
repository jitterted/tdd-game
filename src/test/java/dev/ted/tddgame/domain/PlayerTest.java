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
    class CommandsGenerateEvents {

        @Test
        void canDrawCardWhenHandHasFewerThanFiveCards() {
            ActionCardDeck actionCardDeck = ActionCardDeck.createForTest(
                    new CardsFactory().allActionCards());
            Fixture fixture = createPlayerWithEventAccumulator(actionCardDeck);
            Player player = fixture.player();

            player.drawCardFrom(actionCardDeck);
            player.drawCardFrom(actionCardDeck);
            player.drawCardFrom(actionCardDeck);
            player.drawCardFrom(actionCardDeck);
            player.drawCardFrom(actionCardDeck);

            new EventsAssertion(fixture.eventEnqueuer().events())
                    .hasExactly(PlayerDrewActionCard.class, 5);
        }

        @Test
        void exceptionThrownWhenDrawCardAndHandHasFiveCards() {
            Player player = Player.createNull(73L, "Player Name");
            ActionCardDeck actionCardDeck = ActionCardDeck
                    .createForTest(new CardsFactory().allActionCards());
            for (int i = 0; i < 5; i++) {
                player.drawCardFrom(actionCardDeck);
            }

            assertThatExceptionOfType(HandAlreadyFull.class)
                    .isThrownBy(() -> player.drawCardFrom(actionCardDeck))
                    .withMessage("Can't draw any more cards, the Hand is full with five cards");
        }

        @Test
        void techNeglectCardDrawnEventsWhenPlayerDrawsTechNeglectCards() {
            ActionCardDeck actionCardDeck = ActionCardDeck.createForTest(
                    ActionCard.CANT_ASSERT,
                    ActionCard.CODE_BLOAT
            );
            Fixture fixture = createPlayerWithEventAccumulator(actionCardDeck);

            fixture.player().drawCardFrom(actionCardDeck);
            fixture.player().drawCardFrom(actionCardDeck);

            assertThat(fixture.eventEnqueuer().events())
                    .containsExactly(
                            new PlayerDrewTechNeglectCard(
                                    fixture.player().memberId(),
                                    ActionCard.CANT_ASSERT),
                            new PlayerDrewTechNeglectCard(
                                    fixture.player().memberId(),
                                    ActionCard.CODE_BLOAT));
        }

        @Test
        void drawTestResultsCard_PlayerDrewTestResultsCard() {
            Player.PlayerAndEventAccumulator playerAndEventAccumulator =
                    Player.createForTestWithEventAccumulator();
            TestResultsCardDeck testResultsCardDeck =
                    TestResultsCardDeck.createForTest(
                            TestResultsCard.NEED_ONE_LESS_CODE,
                            TestResultsCard.AS_PREDICTED);
            Player player = playerAndEventAccumulator.player();

            player.drawTestResultsCardFrom(testResultsCardDeck);

            assertThat(playerAndEventAccumulator
                               .accumulatingEventEnqueuer()
                               .events())
                    .containsExactly(
                            new PlayerDrewTestResultsCard(
                                    player.memberId(),
                                    TestResultsCard.NEED_ONE_LESS_CODE
                            )
                    );
        }

        @Test
        void exceptionThrownWhenDrawingTestResultsCardTwiceWithoutDiscarding() {
            Player.PlayerAndEventAccumulator playerAndEventAccumulator = Player.createForTestWithEventAccumulator();
            Player player = playerAndEventAccumulator.player();
            TestResultsCardDeck testResultsCardDeck =
                    TestResultsCardDeck.createForTest(
                            TestResultsCard.AS_PREDICTED,
                            TestResultsCard.NEED_TWO_LESS_CODE);
            player.drawTestResultsCardFrom(testResultsCardDeck);
            player.apply((PlayerEvent) playerAndEventAccumulator.accumulatingEventEnqueuer().events().getFirst());

            assertThatIllegalStateException()
                    .isThrownBy(() -> player.drawTestResultsCardFrom(testResultsCardDeck));
        }

        // -- FIXTURE

        private Fixture createPlayerWithEventAccumulator(ActionCardDeck actionCardDeck) {
            final PlayerId playerId = new PlayerId(IRRELEVANT_PLAYER_ID);
            Player.AccumulatingEventEnqueuer eventEnqueuer = new Player.AccumulatingEventEnqueuer();
            Player player = new Player(playerId,
                                       new MemberId(IRRELEVANT_MEMBER_ID),
                                       "Player 1",
                                       eventEnqueuer,
                                       new Workspace(playerId));
            return new Fixture(eventEnqueuer, player, actionCardDeck);
        }

        private record Fixture(Player.AccumulatingEventEnqueuer eventEnqueuer, Player player,
                               ActionCardDeck actionCardDeck) {}

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

        @Test
        void drawTechNeglectCardsGoDirectlyIntoWorkspace() {
            MemberId memberId = new MemberId(82L);
            Player player = createPlayer(memberId.id());
            List<PlayerEvent> events = List.of(
                    new PlayerDrewTechNeglectCard(memberId, ActionCard.CANT_ASSERT),
                    new PlayerDrewTechNeglectCard(memberId, ActionCard.CODE_BLOAT)
            );

            events.forEach(player::apply);

            assertThat(player.hand())
                    .as("No cards should have been added to the hand, as we drew only Tech Neglect cards")
                    .isEmpty();
            assertThat(player.workspace().techNeglectCards())
                    .containsExactly(ActionCard.CANT_ASSERT, ActionCard.CODE_BLOAT);
        }
    }


    private static Player createPlayer(long memberId) {
        final PlayerId playerId = new PlayerId(IRRELEVANT_PLAYER_ID);
        return new Player(playerId,
                          new MemberId(memberId),
                          "Player 1",
                          null,
                          new Workspace(playerId));
    }
}