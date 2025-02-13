package dev.ted.tddgame.domain;

import dev.ted.tddgame.adapter.in.web.GameScenarioBuilder;
import dev.ted.tddgame.application.port.GameStore;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
    void createGameWithDefinedShufflerForActionCardDeck() {
        Deck.Shuffler<ActionCard> refactorCardsOnlyShuffler =
                _ -> new ArrayList<>(Collections.nCopies(12, ActionCard.REFACTOR));
        Game game = Game.createNull(refactorCardsOnlyShuffler, "Name of Game", "game-handle");
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
        void creatingGameEmits_GameCreated_ActionCardDeckCreated_Events() {
            Game game = new Game.GameFactory().create("game name", "lovely-dog-23");

            Stream<GameEvent> events = game.freshEvents();

            assertThat(events)
                    .hasSize(2)
                    .startsWith(new GameCreated("game name", "lovely-dog-23"));

            assertThat(game.freshEvents())
                    .hasExactlyElementsOfTypes(
                            GameCreated.class,
                            ActionCardDeckCreated.class);

            ActionCardDeckCreated actionCardDeckCreated =
                    game.freshEvents()
                        .filter(event -> event instanceof ActionCardDeckCreated)
                        .map(event -> (ActionCardDeckCreated) event)
                        .findFirst()
                        .get();

            assertThat(actionCardDeckCreated.actionCards())
                    .as("Number of action cards in deck created event must be 63")
                    .hasSize(63);
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
        void startGameEmits_GameStarted_PlayerDrawRegularCards_Events() {
            Game.GameFactory gameFactory = new Game
                    .GameFactory(new Deck.IdentityShuffler<>());
            Game gameForSetup = gameFactory.create("IRRELEVANT NAME", "IRRELEVANT HANDLE");
            GameStore gameStore = GameStore.createEmpty(gameFactory);
            gameForSetup.join(new MemberId(1L), "IRRELEVANT PLAYER NAME");
            gameStore.save(gameForSetup);
            // get the Game, but with no fresh events
            Game game = gameStore.findByHandle(gameForSetup.handle())
                                 .orElseThrow();

            game.start();

            assertThat(game.freshEvents())
                    .hasExactlyElementsOfTypes(
                            GameStarted.class,
                            DeckReplenished.class,
                            CardDrawn.class, PlayerDrewActionCard.class,
                            CardDrawn.class, PlayerDrewActionCard.class,
                            CardDrawn.class, PlayerDrewActionCard.class,
                            CardDrawn.class, PlayerDrewActionCard.class,
                            CardDrawn.class, PlayerDrewActionCard.class);
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

            Game game = gameScenarioBuilder.game();
            game.start();

            // don't reconstitute the game, we want to see the fresh events
            new EventsAssertion(game.freshEvents())
                    .hasExactly(PlayerDrewActionCard.class, 2 * Player.PLAYER_HAND_FULL_SIZE)
                    .hasExactly(CardDrawn.class, 2 * Player.PLAYER_HAND_FULL_SIZE);
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

            Game game = gameScenarioBuilder.game();
            game.start();

            // don't reconstitute the game, we want to see the fresh events
            new EventsAssertion(game.freshEvents())
                    .as("12 cards needed to be drawn, as there are 2 Tech Neglect cards and 10 Regular Action Cards")
                    .hasExactly(CardDrawn.class, 12)
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
                            , new CardDiscarded(ActionCard.PREDICT)
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
            Game.GameFactory gameFactory = new Game.GameFactory(new Deck.IdentityShuffler<>());
            Game gameSetup = gameFactory.create("Irrelevant Game Name", "irrelevant-game-handle");
            gameSetup.join(firstPlayerMemberId, "first player");
            gameSetup.join(secondPlayerMemberId, "second player");
            gameSetup.start();

            Game game = gameFactory.reconstitute(gameSetup.freshEvents().toList());
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
            assertThat(game.actionCardDeck()
                           .discardPile())
                    .as("discard pile should be empty")
                    .isEmpty();
        }

        @Test
        void playerDiscardedCardResultsInCardMovedFromPlayerHandToDeckDiscardPile() {
            MemberId firstPlayerMemberId = new MemberId(71L);
            Game game = createTwoPlayerStartedGame(firstPlayerMemberId,
                                                   ActionCard.WRITE_CODE,
                                                   ActionCard.PREDICT,
                                                   ActionCard.REFACTOR,
                                                   ActionCard.LESS_CODE,
                                                   ActionCard.LESS_CODE);

            game.discard(firstPlayerMemberId, ActionCard.WRITE_CODE);

            Player player = game.playerFor(firstPlayerMemberId);
            assertThat(player.hand())
                    .as("Hand should have discarded the WRITE_CODE card")
                    .containsExactly(ActionCard.PREDICT, ActionCard.REFACTOR,
                                     ActionCard.LESS_CODE, ActionCard.LESS_CODE);
            assertThat(game.actionCardDeck()
                           .discardPile())
                    .as("Action Deck Discard pile should contain only discarded WRITE_CODE cards")
                    .containsExactly(ActionCard.WRITE_CODE);
            assertThat(player.workspace().currentHexTile())
                    .as("Player should be on the 2nd tile after the Discard Card action")
                    .isEqualByComparingTo(HexTile.HOW_WILL_YOU_KNOW_IT_DID_IT);
        }

        @Test
        void playerPlayedCardResultsInCardMovedFromPlayerHandToWorkspace() {
            MemberId firstPlayerMemberId = new MemberId(82L);
            Game game = createTwoPlayerStartedGame(firstPlayerMemberId,
                                                   ActionCard.WRITE_CODE,
                                                   ActionCard.PREDICT,
                                                   ActionCard.REFACTOR,
                                                   ActionCard.LESS_CODE,
                                                   ActionCard.LESS_CODE);
            game.discard(firstPlayerMemberId, ActionCard.PREDICT);
            game.discard(firstPlayerMemberId, ActionCard.REFACTOR);

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

        //--

        private static Game createTwoPlayerStartedGame(MemberId firstPlayerMemberId, ActionCard... actionCards) {
            Deck.Shuffler<ActionCard> configuredActionCardShuffler =
                    _ -> new ArrayList<>(List.of(
                            // each player grabs all the cards to fill their hand first
                            actionCards[0],
                            actionCards[1],
                            actionCards[2],
                            actionCards[3],
                            actionCards[4],
                            // now the cards for the 2nd player:
                            ActionCard.WRITE_CODE,
                            ActionCard.PREDICT,
                            ActionCard.REFACTOR,
                            ActionCard.LESS_CODE,
                            ActionCard.LESS_CODE
                    ));
            Game game = new Game.GameFactory(configuredActionCardShuffler)
                    .create("Irrelevant Name", "irrelevant-handle");
            game.join(firstPlayerMemberId, "Member = First Player");
            game.join(new MemberId(99L), "Irrelevant Member");
            game.start();
            return game;
        }

        private static List<GameEvent> gameCreatedAndTheseEvents(GameEvent... freshEvents) {
            List<GameEvent> events = new ArrayList<>();
            events.add(new GameCreated("jitterted", "breezy-cat-85"));
            events.addAll(List.of(freshEvents));
            return events;
        }
    }

}