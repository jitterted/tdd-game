package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
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
    class CommandGeneratesEvent {

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3, 4})
        void handHasRoomForCardDrawnFromDeckWhenHandHasNCards(int startingHandSize) {
            PlayerAndCardFixture fixture =
                    createPlayerDealtCardsOfQuantity(startingHandSize);

            List<GameEvent> gameEvents =
                    fixture.player().drawCardFrom(fixture.actionCardDeck());

            assertThat(gameEvents)
                    .hasExactlyElementsOfTypes(PlayerDrewActionCard.class);
        }


        @Test
        void exceptionThrownWhenDrawCardAndHandHasFiveCards() {
            PlayerAndCardFixture fixture = createPlayerDealtCardsOfQuantity(5);

            assertThatExceptionOfType(HandAlreadyFull.class)
                    .as("Expected 'HandAlreadyFull' exception to have been thrown.")
                    .isThrownBy(() -> fixture.player().drawCardFrom(fixture.actionCardDeck()))
                    .withMessage("Can't draw any more cards, the Hand is full with five cards");
        }

        @Test
        void techNeglectCardDrawnEventGeneratedWhenPlayerDrawsTechNeglectCard() {
            Player player = createNewPlayer();
            ActionCardDeck actionCardDeck = ActionCardDeck.createForTest();
            actionCardDeck.apply(new ActionCardDeckReplenished(
                    List.of(ActionCard.CANT_ASSERT,
                            ActionCard.CODE_BLOAT)));

            assertThat(player.drawCardFrom(actionCardDeck))
                    .containsExactly(
                            new PlayerDrewTechNeglectCard(
                                    player.memberId(),
                                    ActionCard.CANT_ASSERT));
        }

        @Test
        void drawTestResultsCard_PlayerDrewTestResultsCard() {
            Player player = createNewPlayer();
            TestResultsCardDeck testResultsCardDeck =
                    TestResultsCardDeck.createForTest(Collections.emptyList());
            testResultsCardDeck.apply(
                    new TestResultsCardDeckReplenished(List.of(
                            TestResultsCard.NEED_ONE_LESS_CODE,
                            TestResultsCard.AS_PREDICTED)));

            List<GameEvent> gameEvents = player.drawTestResultsCardFrom(testResultsCardDeck);

            assertThat(gameEvents)
                    .containsExactly(
                            new PlayerDrewTestResultsCard(
                                    player.memberId(),
                                    TestResultsCard.NEED_ONE_LESS_CODE
                            )
                    );
        }

        @Test
        void exceptionThrownWhenDrawingTestResultsCardTwiceWithoutDiscarding() {
            Player player = createNewPlayer();
            TestResultsCardDeck testResultsCardDeck = TestResultsCardDeck.createForTest();
            testResultsCardDeck.apply(new TestResultsCardDeckReplenished(
                    List.of(TestResultsCard.NEED_TWO_LESS_CODE)));
            player.apply(new PlayerDrewTestResultsCard(player.memberId(),
                                                       TestResultsCard.AS_PREDICTED));

            assertThatIllegalStateException()
                    .isThrownBy(() -> player.drawTestResultsCardFrom(testResultsCardDeck));
        }

    }

    private PlayerAndCardFixture createPlayerDealtCardsOfQuantity(int startingHandSize) {
        List<RegularCard> cards = List.of(ActionCard.WRITE_CODE,
                                          ActionCard.LESS_CODE,
                                          ActionCard.LESS_CODE,
                                          ActionCard.PREDICT,
                                          ActionCard.WRITE_CODE);
        Player player = createNewPlayer();
        for (int i = 0; i < startingHandSize; i++) {
            player.apply(new PlayerDrewActionCard(player.memberId(),
                                                  cards.get(i)));
        }
        ActionCardDeck actionCardDeck = ActionCardDeck.createForTest();
        actionCardDeck.apply(new ActionCardDeckReplenished(
                List.of(ActionCard.PREDICT)));
        return new PlayerAndCardFixture(player, actionCardDeck);
    }

    private record PlayerAndCardFixture(Player player, ActionCardDeck actionCardDeck) {}

    private static Player createNewPlayer() {
        return new Player(IRRELEVANT_PLAYER_ID,
                          IRRELEVANT_MEMBER_ID,
                          "Player 1",
                          new Workspace(IRRELEVANT_PLAYER_ID));
    }

    @Nested
    class EventsProjectState {

        @Test
        void drewActionCardTwiceResultsInTwoCardsInHand() {
            MemberId memberId = new MemberId(37L);
            Player player = createPlayer(new MemberId(memberId.id()));
            List<GameEvent> events = List.of(
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
            List<GameEvent> events = List.of(
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
            List<GameEvent> events = List.of(
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
                          new Workspace(playerId));
    }
}