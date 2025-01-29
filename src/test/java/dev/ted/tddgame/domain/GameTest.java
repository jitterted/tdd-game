package dev.ted.tddgame.domain;

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

        private static final int PLAYER_HAND_FULL_SIZE = 5;

        @Test
        void creatingGameEmitsGameCreatedEvent() {
            Game game = new Game.GameFactory().create("game name", "lovely-dog-23");

            Stream<GameEvent> events = game.freshEvents();

            assertThat(events)
                    .containsExactly(new GameCreated("game name", "lovely-dog-23"));
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
        void startGameEmitsGameStarted_DeckCreated_PlayerDrawCards_Events() {
            List<GameEvent> committedEvents = List.of(
                    new GameCreated("IRRELEVANT NAME", "IRRELEVANT HANDLE"),
                    new PlayerJoined(new MemberId(1L), "player 1"));
            Game game = new Game.GameFactory().reconstitute(committedEvents);

            game.start();

            assertThat(game.freshEvents())
                    .hasExactlyElementsOfTypes(
                            GameStarted.class,
                            ActionCardDeckCreated.class,
                            ActionCardDeckReplenished.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class,
                            ActionCardDrawn.class, PlayerDrewActionCard.class);

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
        void withMultiplePlayers_AllPlayersHaveFullHands() {
            List<GameEvent> committedEvents = List.of(
                    new GameCreated("IRRELEVANT NAME", "IRRELEVANT HANDLE"),
                    new PlayerJoined(new MemberId(1L), "player 1"),
                    new PlayerJoined(new MemberId(2L), "player 2")
            );
            Game game = new Game.GameFactory().reconstitute(committedEvents);

            game.start();

            new EventsAssertion(game.freshEvents())
                    .hasExactly(PlayerDrewActionCard.class, 2 * PLAYER_HAND_FULL_SIZE);

            new EventsAssertion(game.freshEvents())
                    .hasExactly(ActionCardDrawn.class, 2 * PLAYER_HAND_FULL_SIZE);
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
            Game game = createFreshGameWithTwoPlayersAndStart(firstPlayerMemberId);

            game.playCard(firstPlayerMemberId, ActionCard.WRITE_CODE);

            assertThat(game.freshEvents())
                    .containsExactly(
                            new PlayerPlayedActionCard(firstPlayerMemberId,
                                                       ActionCard.WRITE_CODE)
                    );
        }

        private static Game createFreshGameWithTwoPlayersAndStart(MemberId firstPlayerMemberId) {
            Game game = new Game.GameFactory().create("IRRELEVANT GAME NAME", "IRRELEVANT HANDLE");
            game.join(firstPlayerMemberId, "first player");
            game.join(new MemberId(113L), "second player");
            game.start();
            // reconstitute the game from the events generated so far
            // as a way to clear the freshEvents()
            return new Game.GameFactory().reconstitute(game.freshEvents().toList());
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
            Game game = gameCreatedAndReconstitutedWithTheseEvents(
                    new PlayerJoined(firstPlayerMemberId, "Member 96 Name"),
                    new PlayerJoined(secondPlayerMemberId, "Member 52 Name"),
                    new GameStarted());
            Player firstPlayer = game.playerFor(firstPlayerMemberId);
            Player secondPlayer = game.playerFor(secondPlayerMemberId);

            assertThat(game.state())
                    .isEqualByComparingTo(Game.State.IN_PROGRESS);
            assertThat(firstPlayer.workspace().currentHexTile())
                    .as("Both players should be on the first Hex Tile (What Should It Do?)")
                    .isEqualByComparingTo(secondPlayer.workspace().currentHexTile())
                    .isEqualByComparingTo(HexTile.WHAT_SHOULD_IT_DO);
        }

        private Game gameCreatedAndReconstitutedWithTheseEvents(GameEvent... newEvents) {
            List<GameEvent> events = gameCreatedAndTheseEvents(
                    newEvents);
            return new Game.GameFactory().reconstitute(events);
        }

        @Test
        void playerDrewCardResultsInPlayerHavingCardDrawnAndDeckDrawPileEmpty() {
            // TODO: replace all of these event instantiations with more encapsulation and Collaborator-Based Isolation?
            List<GameEvent> events = gameCreatedAndTheseEvents(
                    new PlayerJoined(new MemberId(53L), "Member 53 Name"),
                    new GameStarted(),
                    new ActionCardDeckCreated(List.of(
                            ActionCard.PREDICT,
                            ActionCard.REFACTOR)),
                    new ActionCardDeckReplenished(List.of(
                            ActionCard.PREDICT,
                            ActionCard.REFACTOR)),
                    new ActionCardDrawn(ActionCard.PREDICT),
                    new PlayerDrewActionCard(new MemberId(53L), ActionCard.PREDICT));
            Game game = new Game.GameFactory().reconstitute(events);

            Player player = game.playerFor(new MemberId(53L));
            assertThat(player.hand())
                    .as("Hand had unexpected cards")
                    .containsExactly(ActionCard.PREDICT);
            assertThat(game.actionCardDeck()
                           .drawPile())
                    .as("draw pile was not as expected")
                    .containsExactly(ActionCard.REFACTOR);
            assertThat(game.actionCardDeck()
                           .discardPile())
                    .as("discard pile should be empty")
                    .isEmpty();
        }

        @Test
        void playerDiscardedCardResultsInCardMovedFromPlayerHandToDeckDiscardPile() {
            MemberId memberId = new MemberId(71L);
//            Game game = new Game.GameFactory().
            List<GameEvent> events = gameCreatedAndTheseEvents(
                    new PlayerJoined(memberId, "Member 71 Name")
                    , new GameStarted()
                    , new ActionCardDeckCreated(List.of(
                            ActionCard.WRITE_CODE,
                            ActionCard.PREDICT,
                            ActionCard.REFACTOR))
                    , new ActionCardDeckReplenished(List.of(
                            ActionCard.WRITE_CODE,
                            ActionCard.PREDICT,
                            ActionCard.REFACTOR))
                    , new ActionCardDrawn(ActionCard.WRITE_CODE)
                    , new PlayerDrewActionCard(memberId, ActionCard.WRITE_CODE)
                    , new ActionCardDrawn(ActionCard.PREDICT)
                    , new PlayerDrewActionCard(memberId, ActionCard.PREDICT)
                    , new PlayerDiscardedActionCard(memberId, ActionCard.WRITE_CODE)
                    , new ActionCardDiscarded(ActionCard.WRITE_CODE)
            );
            Game game = new Game.GameFactory().reconstitute(events);

            Player player = game.playerFor(memberId);
            assertThat(player.hand())
                    .as("Hand should have discarded the WRITE_CODE card")
                    .containsExactly(ActionCard.PREDICT);
            assertThat(game.actionCardDeck()
                           .discardPile())
                    .as("Action Deck Discard pile should contain only discarded WRITE_CODE cards")
                    .containsExactly(ActionCard.WRITE_CODE);
            assertThat(player.workspace().currentHexTile())
                    .as("Player should be on the 2nd tile after the Discard Card action")
                    .isEqualByComparingTo(HexTile.HOW_WILL_YOU_KNOW_IT_DID_IT);
        }

        private static List<GameEvent> gameCreatedAndTheseEvents(GameEvent... freshEvents) {
            List<GameEvent> events = new ArrayList<>();
            events.add(new GameCreated("jitterted", "breezy-cat-85"));
            events.addAll(List.of(freshEvents));
            return events;
        }
    }

}