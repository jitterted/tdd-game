package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerTest {

    private static final PlayerId IRRELEVANT_PLAYER_ID = new PlayerId(1L);
    private static final MemberId IRRELEVANT_MEMBER_ID = new MemberId(213L);

    @Test
    void newPlayerHasEmptyHand() {
        Player player = createPlayer(IRRELEVANT_MEMBER_ID);

        assertThat(player.hand())
                .isEmpty();
    }

    @Nested
    class CommandsGenerateEvents {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5})
        void canDrawCardWhenHandHasFewerThanNCards(int drawCount) {
            Player player = createNewPlayer();
            ActionCardDeck actionCardDeck = ActionCardDeck.createForTest(
                    new CardsFactory().allActionCards());
            for (int i = 0; i < drawCount - 1; i++) {
                player.apply(new PlayerDrewActionCard(player.memberId(), actionCardDeck.draw()));
            }

            List<GameEvent> gameEvents = player.drawCardFrom(actionCardDeck);

            new EventsAssertion(gameEvents)
                    .hasExactly(PlayerDrewActionCard.class, 1);
        }

        @Test
        void exceptionThrownWhenDrawCardAndHandHasFiveCards() {
            Player player = createNewPlayer();
            ActionCardDeck actionCardDeck = ActionCardDeck
                    .createForTest(new CardsFactory().allActionCards());
            for (int i = 0; i < 5; i++) {
                player.apply(new PlayerDrewActionCard(player.memberId(), actionCardDeck.draw()));
            }

            assertThatExceptionOfType(HandAlreadyFull.class)
                    .as("Expected 'HandAlreadyFull' exception to have been thrown.")
                    .isThrownBy(() -> player.drawCardFrom(actionCardDeck))
                    .withMessage("Can't draw any more cards, the Hand is full with five cards");
        }

        @Test
        void techNeglectCardDrawnEventsWhenPlayerDrawsTechNeglectCards() {
            ActionCardDeck actionCardDeck = ActionCardDeck.createForTest(
                    ActionCard.CANT_ASSERT,
                    ActionCard.CODE_BLOAT
            );
            Player player = createNewPlayer();

            assertThat(player.drawCardFrom(actionCardDeck))
                    .containsExactly(
                            new PlayerDrewTechNeglectCard(
                                    player.memberId(),
                                    ActionCard.CANT_ASSERT));
        }

        @Test
        void drawTestResultsCard_PlayerDrewTestResultsCard() {
            Player.PlayerAndEventAccumulator playerAndEventAccumulator =
                    Player.createForTestWithEventAccumulator();
            Player player = playerAndEventAccumulator.player();
            TestResultsCardDeck testResultsCardDeck =
                    TestResultsCardDeck.createForTest(
                            TestResultsCard.NEED_ONE_LESS_CODE,
                            TestResultsCard.AS_PREDICTED);

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
            Player player = Player.createForTestWithApplyingEnqueuer();
            TestResultsCardDeck testResultsCardDeck =
                    TestResultsCardDeck.createForTest(
                            TestResultsCard.AS_PREDICTED,
                            TestResultsCard.NEED_TWO_LESS_CODE);
            player.drawTestResultsCardFrom(testResultsCardDeck);

            assertThatIllegalStateException()
                    .isThrownBy(() -> player.drawTestResultsCardFrom(testResultsCardDeck));
        }

        // -- FIXTURE

        private Fixture createPlayerWithEventAccumulator(ActionCardDeck actionCardDeck) {
            final PlayerId playerId = IRRELEVANT_PLAYER_ID;
            Player.AccumulatingEventEnqueuer eventEnqueuer = new Player.AccumulatingEventEnqueuer();
            Player player = new Player(playerId,
                                       IRRELEVANT_MEMBER_ID,
                                       "Player 1",
                                       eventEnqueuer,
                                       new Workspace(playerId));
            return new Fixture(eventEnqueuer, player, actionCardDeck);
        }

        private record Fixture(Player.AccumulatingEventEnqueuer eventEnqueuer, Player player,
                               ActionCardDeck actionCardDeck) {}

    }

    private static Player createNewPlayer() {
        return new Player(IRRELEVANT_PLAYER_ID,
                          IRRELEVANT_MEMBER_ID,
                          "Player 1",
                          _ -> {},
                          new Workspace(IRRELEVANT_PLAYER_ID));
    }

    @Nested
    class EventsProjectState {

        @Test
        void drewActionCardTwiceResultsInTwoCardsInHand() {
            MemberId memberId = new MemberId(37L);
            Player player = createPlayer(new MemberId(memberId.id()));
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
            Player player = createPlayer(new MemberId(memberId.id()));
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
            Player player = createPlayer(new MemberId(memberId.id()));
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


    private static Player createPlayer(MemberId memberId) {
        final PlayerId playerId = IRRELEVANT_PLAYER_ID;
        return new Player(playerId,
                          memberId,
                          "Player 1",
                          null,
                          new Workspace(playerId));
    }
}