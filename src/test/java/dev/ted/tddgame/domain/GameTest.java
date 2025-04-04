package dev.ted.tddgame.domain;

import dev.ted.tddgame.adapter.in.web.GameScenarioBuilder;
import dev.ted.tddgame.application.port.GameStore;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void newGameAssignedIdentifier() {
        Game game = new Game.GameFactory().create("name", "windy-dolphin-68");

        assertThat(game.handle())
                .isNotNull()
                .isNotBlank();
    }

    @Test
    void reconstitutedGameFromEventsHasNoFreshEvents() {
        Game reconstituted = new Game.GameFactory().reconstitute(List.of(
                new GameCreated("name", "snowy-hound-21"),
                new PlayerJoined(new MemberId(1L), "player 1")
        ));

        assertThat(reconstituted.freshEvents())
                .isEmpty();
    }

    @Test
    void createGameWithDefinedCardsForActionCardDeck() {
        CardsFactory cardsFactory = CardsFactory.forTest(
                Collections.nCopies(12, ActionCard.REFACTOR));
        Game.GameFactory gameFactory = new Game.GameFactory(
                new Deck.IdentityShuffler<>(),
                cardsFactory);
        Game game = gameFactory.create("Name of Game", "game-handle");
        MemberId firstPlayerMemberId = new MemberId(23L);
        game.join(firstPlayerMemberId, "First Player");
        MemberId secondPlayerMemberId = new MemberId(24L);
        game.join(secondPlayerMemberId, "Second Player");

        game.start();

        assertThat(game.playerFor(firstPlayerMemberId).hand())
                .containsOnly(ActionCard.REFACTOR);
        assertThat(game.playerFor(secondPlayerMemberId).hand())
                .containsOnly(ActionCard.REFACTOR);
    }

    @Nested
    class CommandsGenerateEvents {

        @Test
        void creatingGameEmits_GameCreated_BothDeckCreated_Events() {
            Game game = new Game.GameFactory().create("game name", "lovely-dog-23");

            assertThat(game.freshEvents())
                    .hasExactlyElementsOfTypes(
                            GameCreated.class,
                            ActionCardDeckCreated.class,
                            TestResultsCardDeckCreated.class)
                    .startsWith(new GameCreated("game name", "lovely-dog-23"));

            new EventsAssertion(game.freshEvents())
                    .hasEventMatching(
                            new Condition<>(gameEvent -> gameEvent.getClass() == ActionCardDeckCreated.class,
                                            "ActionCardDeckCreated event not found"),
                            new Condition<>(gameEvent -> ((ActionCardDeckCreated) gameEvent).actionCards().size() == 63,
                                            "63 cards in the Action Card Deck"))
                    .hasEventMatching(
                            new Condition<>(gameEvent -> gameEvent.getClass() == TestResultsCardDeckCreated.class,
                                            "TestResultsCardDeckCreated event not found"),
                            new Condition<>(gameEvent -> ((TestResultsCardDeckCreated) gameEvent).testResultsCards().size() == 18,
                                            "18 cards in the Test Results Card Deck")
                    );
        }

        @Test
        void memberJoiningEmitsPlayerJoinedEvent() {
            Game game = createFreshGame();

            game.join(new MemberId(88L), "player name");

            assertThat(game.freshEvents())
                    .containsExactly(new PlayerJoined(new MemberId(88L), "player name"));
        }

        @Test
        void memberJoiningTwiceDoesNotEmitNewPlayerJoinedEvent() {
            Game game = createFreshGame();

            game.join(new MemberId(88L), "player name");
            game.join(new MemberId(88L), "player name");

            assertThat(game.freshEvents())
                    .containsOnlyOnce(new PlayerJoined(new MemberId(88L), "player name"));
        }

        @Test
        void startGameEmits_GameStarted_PlayerDrewActionCards_Events() {
            Game game = GameScenarioBuilder.create()
                                           .unshuffledActionCards()
                                           .memberJoinsAsPlayer(new MemberId(1L))
                                           .reconstitutedGameFromStore();

            game.start();

            assertThat(game.freshEvents())
                    .hasExactlyElementsOfTypes(
                            GameStarted.class,
                            ActionCardDeckReplenished.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class);
        }

        @Test
        void withMultiplePlayers_OnlyRegularActionCardsInDeck_AllPlayersHaveFullHands() {
            GameScenarioBuilder gameScenarioBuilder = GameScenarioBuilder
                    .create()
                    .actionCards(3, ActionCard.PREDICT,
                                 4, ActionCard.LESS_CODE,
                                 1, ActionCard.REFACTOR,
                                 2, ActionCard.WRITE_CODE)
                    .memberJoinsAsPlayer(new MemberId(10L))
                    .memberJoinsAsPlayer(new MemberId(11L));

            Game game = gameScenarioBuilder.reconstitutedGameFromStore();
            game.start();

            // don't reconstitute the game, we want to see the fresh events
            new EventsAssertion(game.freshEvents())
                    .hasExactly(PlayerDrewActionCard.class, 2 * Player.PLAYER_HAND_FULL_SIZE)
                    .hasExactly(ActionCardDrawn.class, 2 * Player.PLAYER_HAND_FULL_SIZE);
        }

        @Test
        void withMultiplePlayers_MixOfRegularAndTechNeglectCardsInDeck_AllPlayersHaveFullHands() {
            GameScenarioBuilder gameScenarioBuilder = GameScenarioBuilder
                    .create()
                    .actionCards(3, ActionCard.PREDICT,
                                 2, ActionCard.LESS_CODE,
                                 1, ActionCard.CODE_BLOAT,
                                 1, ActionCard.CANT_ASSERT,
                                 1, ActionCard.REFACTOR,
                                 4, ActionCard.WRITE_CODE)
                    .memberJoinsAsPlayer(new MemberId(10L))
                    .memberJoinsAsPlayer(new MemberId(11L));

            Game game = gameScenarioBuilder.reconstitutedGameFromStore();
            game.start();

            // don't reconstitute the game, we want to see the fresh events
            new EventsAssertion(game.freshEvents())
                    .as("12 cards needed to be drawn, as there are 2 Tech Neglect cards and 10 Regular Action Cards")
                    .hasExactly(ActionCardDrawn.class, 12)
                    .as("10 Action Cards should have been drawn")
                    .hasExactly(PlayerDrewActionCard.class, 2 * Player.PLAYER_HAND_FULL_SIZE)
                    .as("2 Tech Neglect cards should have been drawn")
                    .hasExactly(PlayerDrewTechNeglectCard.class, 2);
        }

        @Test
        void playerDiscardsActionCard_PlayerCardDiscarded_ActionCardDiscarded() {
            MemberId firstPlayerMemberId = new MemberId(88L);
            Game game = createFreshGameWithTwoPlayersAndStart(firstPlayerMemberId);

            game.discard(firstPlayerMemberId, ActionCard.PREDICT);

            assertThat(game.freshEvents())
                    .containsExactly(
                            new PlayerDiscardedActionCard(firstPlayerMemberId,
                                                          ActionCard.PREDICT)
                            , new ActionCardDiscarded(ActionCard.PREDICT)
                    );
        }

        @Test
        void playerPlaysActionCard_PlayerPlayedActionCard() {
            MemberId firstPlayerMemberId = new MemberId(88L);
            Game game = createStartedGameWithTwoPlayersAndPlayerOnThirdTile(firstPlayerMemberId);

            game.playCard(firstPlayerMemberId, ActionCard.WRITE_CODE);

            assertThat(game.freshEvents())
                    .containsExactly(
                            new PlayerPlayedActionCard(firstPlayerMemberId,
                                                       ActionCard.WRITE_CODE)
                    );
        }

        @Test
        void drawTestResultsCard_PlayerDrewTestResultsCard_and_DeckEvents() {
            TestResultsCard cardToBeDrawn = TestResultsCard.NEED_ONE_LESS_CODE;
            TestResultsCard cardRemainingInDrawPile = TestResultsCard.AS_PREDICTED;
            String gameHandle = "draw-test-results-card-scenario";
            GameScenarioBuilder gameScenarioBuilder = GameScenarioBuilder
                    .scenarioPlayerOnPredictTestWillFailToCompile(
                            gameHandle,
                            cardToBeDrawn,
                            cardRemainingInDrawPile);
            Game game = gameScenarioBuilder.reconstitutedGameFromStore();
            MemberId memberId = gameScenarioBuilder.firstPlayer().memberId();

            game.drawTestResultsCard(memberId);

            assertThat(game.freshEvents())
                    .containsExactly(
                            new TestResultsCardDeckReplenished(
                                    List.of(cardToBeDrawn,
                                            cardRemainingInDrawPile)),
                            new TestResultsCardDrawn(cardToBeDrawn),
                            new PlayerDrewTestResultsCard(memberId,
                                                          cardToBeDrawn)
                    );
        }

        // ---- FIXTURES ----

        private Game createStartedGameWithTwoPlayersAndPlayerOnThirdTile(MemberId firstPlayerMemberId) {
            GameStore gameStore = GameStore.createEmpty();

            Game game = new Game.GameFactory().create("IRRELEVANT GAME NAME", "started-game");
            game.join(firstPlayerMemberId, "first player");
            game.join(new MemberId(113L), "second player");
            game.start();
            game.discard(firstPlayerMemberId, ActionCard.PREDICT);
            game.discard(firstPlayerMemberId, ActionCard.REFACTOR);

            gameStore.save(game);

            // returns the game with no freshEvents
            return gameStore.findByHandle(game.handle())
                            .orElseThrow();
        }

        private static Game createFreshGameWithTwoPlayersAndStart(MemberId firstPlayerMemberId) {
            Game game = new Game.GameFactory().create("IRRELEVANT GAME NAME", "started-game");
            game.join(firstPlayerMemberId, "first player");
            game.join(new MemberId(113L), "second player");
            game.start();

            GameStore gameStore = GameStore.createEmpty();
            gameStore.save(game);
            return gameStore.findByHandle(game.handle()).orElseThrow();
        }

        private static Game createFreshGame() {
            return new Game.GameFactory().reconstitute(List.of(new GameCreated("IRRELEVANT NAME", "IRRELEVANT HANDLE")));
        }
    }

    @Nested
    class EventsProjectState {

        @Test
        void newGameHasGameNameAndHandleAndStateIsCreated() {
            List<GameEvent> events = List.of(
                    new GameCreated("jitterted", "breezy-cat-85"));
            Game game = new Game.GameFactory().reconstitute(events);

            assertThat(game.name())
                    .isEqualTo("jitterted");
            assertThat(game.handle())
                    .isEqualTo("breezy-cat-85");
            assertThat(game.state())
                    .isEqualByComparingTo(Game.State.WAITING_TO_START);
        }

        @Test
        void playerJoinedResultsInPlayerAddedToGame() {
            List<GameEvent> events = gameCreatedAndTheseEvents(
                    new PlayerJoined(new MemberId(53L), "Member 53 Name"));

            Game game = new Game.GameFactory().reconstitute(events);

            assertThat(game.players())
                    .hasSize(1)
                    .extracting(Player::memberId, Player::playerName)
                    .containsExactly(tuple(new MemberId(53L), "Member 53 Name"));

            assertThat(game.playerFor(new MemberId(53L))
                           .memberId())
                    .isEqualTo(new MemberId(53L));
        }

        @Test
        void actionCardDeckCreatedResultsInDeckHavingCardsFromEvent() {
            List<GameEvent> events = gameCreatedPlayerJoinedAnd(
                    new ActionCardDeckCreated(List.of(
                            ActionCard.PREDICT,
                            ActionCard.LESS_CODE,
                            ActionCard.PREDICT
                    )));

            Game game = new Game.GameFactory().reconstitute(events);

            assertThat(game.actionCardDeck()
                           .drawPile())
                    .isEmpty();
            assertThat(game.actionCardDeck()
                           .discardPile())
                    .containsExactly(ActionCard.PREDICT,
                                     ActionCard.LESS_CODE,
                                     ActionCard.PREDICT);
        }

        @Test
        void testResultsCardDeckCreatedResultsInDeckHavingCardsFromEvent() {
            List<GameEvent> events = gameCreatedPlayerJoinedAnd(
                    new TestResultsCardDeckCreated(List.of(
                            TestResultsCard.AS_PREDICTED,
                            TestResultsCard.NEED_ONE_LESS_CODE,
                            TestResultsCard.NEED_TWO_LESS_CODE
                    )));

            Game game = new Game.GameFactory().reconstitute(events);

            assertThat(game.testResultsCardDeck().drawPile())
                    .isEmpty();
            assertThat(game.testResultsCardDeck().discardPile())
                    .containsExactly(TestResultsCard.AS_PREDICTED,
                                     TestResultsCard.NEED_ONE_LESS_CODE,
                                     TestResultsCard.NEED_TWO_LESS_CODE);
        }

        private List<GameEvent> gameCreatedPlayerJoinedAnd(GameEvent... freshEvents) {
            List<GameEvent> events = new ArrayList<>();
            events.add(new GameCreated("jitterted", "breezy-cat-85"));
            events.add(new PlayerJoined(new MemberId(42L), "IRRELEVANT PLAYER NAME"));
            events.addAll(List.of(freshEvents));
            return events;
        }

        @Test
        void gameStartedResultsInStateOfGameAsInProgress() {
            MemberId firstPlayerMemberId = new MemberId(96L);
            MemberId secondPlayerMemberId = new MemberId(52L);
            Game game = GameScenarioBuilder.create()
                                           .unshuffledActionCards()
                                           .memberJoinsAsPlayer(firstPlayerMemberId)
                                           .memberJoinsAsPlayer(secondPlayerMemberId)
                                           .startGame()
                                           .reconstitutedGameFromStore();

            Player firstPlayer = game.playerFor(firstPlayerMemberId);
            Player secondPlayer = game.playerFor(secondPlayerMemberId);

            assertThat(game.state())
                    .isEqualByComparingTo(Game.State.IN_PROGRESS);
            assertThat(firstPlayer.workspace().currentHexTile())
                    .as("Both players should be on the first Hex Tile (What Should It Do?)")
                    .isEqualByComparingTo(secondPlayer.workspace().currentHexTile())
                    .isEqualByComparingTo(HexTile.WHAT_SHOULD_IT_DO);
            assertThat(firstPlayer.hand())
                    .as("First player's hand should have 5 cards")
                    .hasSize(5);
            assertThat(secondPlayer.hand())
                    .as("Second player's hand should have 5 cards")
                    .hasSize(5);
            assertThat(game.actionCardDeck().drawPile())
                    .hasSize(63 - 5 - 5);
            assertThat(game.actionCardDeck().discardPile())
                    .as("discard pile should be empty")
                    .isEmpty();
        }

        @Test
        void playerDiscardedCardResultsInCardMovedFromPlayerHandToDeckDiscardPile() {
            MemberId firstPlayerMemberId = new MemberId(71L);
            Game game = GameScenarioBuilder.create()
                                           .actionCards(ActionCard.WRITE_CODE,
                                                        ActionCard.PREDICT,
                                                        ActionCard.REFACTOR,
                                                        ActionCard.LESS_CODE,
                                                        ActionCard.LESS_CODE)
                                           .memberJoinsAsPlayer(firstPlayerMemberId)
                                           .startGame()
                                           .reconstitutedGameFromStore();

            game.discard(firstPlayerMemberId, ActionCard.WRITE_CODE);

            Player player = game.playerFor(firstPlayerMemberId);
            assertThat(player.hand())
                    .as("Hand should have discarded the WRITE_CODE card")
                    .containsExactly(ActionCard.PREDICT, ActionCard.REFACTOR,
                                     ActionCard.LESS_CODE, ActionCard.LESS_CODE);
            assertThat(game.actionCardDeck().discardPile())
                    .as("Action Deck Discard pile should contain only discarded WRITE_CODE cards")
                    .containsExactly(ActionCard.WRITE_CODE);
            assertThat(player.workspace().currentHexTile())
                    .as("Player should be on the 2nd tile after the Discard Card action")
                    .isEqualByComparingTo(HexTile.HOW_WILL_YOU_KNOW_IT_DID_IT);
        }

        @Test
        void playerPlayedCardResultsInCardMovedFromPlayerHandToWorkspace() {
            MemberId firstPlayerMemberId = new MemberId(82L);
            Game game = GameScenarioBuilder.create()
                                           .actionCards(
                                                   ActionCard.WRITE_CODE,
                                                   ActionCard.PREDICT,
                                                   ActionCard.REFACTOR,
                                                   ActionCard.LESS_CODE,
                                                   ActionCard.LESS_CODE)
                                           .memberJoinsAsPlayer(firstPlayerMemberId)
                                           .startGame()
                                           .discard(ActionCard.PREDICT)
                                           .discard(ActionCard.REFACTOR)
                                           .reconstitutedGameFromStore();

            game.playCard(firstPlayerMemberId, ActionCard.WRITE_CODE);

            Player player = game.playerFor(firstPlayerMemberId);
            assertThat(player.hand())
                    .as("Hand should have PLAYed the WRITE_CODE card")
                    .containsExactly(ActionCard.LESS_CODE, ActionCard.LESS_CODE);
            assertThat(game.actionCardDeck().discardPile())
                    .as("Action Deck Discard pile should have 2 cards")
                    .containsExactly(ActionCard.PREDICT, ActionCard.REFACTOR);
            assertThat(player.workspace().currentHexTile())
                    .as("Player should be on the 4th tile after playing the WRITE CODE Card")
                    .isEqualByComparingTo(HexTile.PREDICT_TEST_WILL_FAIL_TO_COMPILE);
            assertThat(player.workspace().cardsInPlay())
                    .as("Workspace should now contain the played WRITE CODE card")
                    .containsExactly(ActionCard.WRITE_CODE);
        }

        @Test
        void workspaceHasTestResultsCardWhenTestResultsCardDrawnByPlayer() {
            GameScenarioBuilder gameScenarioBuilder = GameScenarioBuilder
                    .create()
                    .shuffledActionCards()
                    .testResultsCards(
                            TestResultsCard.NEED_TWO_LESS_CODE,
                            TestResultsCard.AS_PREDICTED,
                            TestResultsCard.NEED_ONE_LESS_CODE
                    )
                    .memberJoinsAsOnlyPlayer()
                    .startGame();
            Game game = gameScenarioBuilder.reconstitutedGameFromStore();
            Player firstPlayer = game.players().getFirst();

            game.drawTestResultsCard(firstPlayer.memberId());

            assertThat(firstPlayer.workspace()
                                  .drawnTestResultsCard())
                    .as("First player's Workspace must have the drawn Test Results card")
                    .isEqualTo(TestResultsCard.NEED_TWO_LESS_CODE);

            assertThat(game.testResultsCardDeck().drawPile())
                    .as("After drawing a card, the remaining cards in the Test Results draw pile must be AS_PREDICTED and NEED_ONE_LESS_CODE")
                    .containsExactly(TestResultsCard.AS_PREDICTED,
                                     TestResultsCard.NEED_ONE_LESS_CODE);
        }

        //--

        private static List<GameEvent> gameCreatedAndTheseEvents(GameEvent... freshEvents) {
            List<GameEvent> events = new ArrayList<>();
            events.add(new GameCreated("jitterted", "breezy-cat-85"));
            events.addAll(List.of(freshEvents));
            return events;
        }
    }

}