package dev.ted.tddgame.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void newGameAssignedIdentifier() {
        Game game = Game.create("name", "windy-dolphin-68");

        assertThat(game.handle())
                .isNotNull()
                .isNotBlank();
    }

    @Test
    void reconstitutedGameFromEventsHasNoFreshEvents() {
        Game reconstituted = Game.reconstitute(List.of(
                new GameCreated("name", "snowy-hound-21"),
                new PlayerJoined(new MemberId(1L), "player 1")
        ));

        assertThat(reconstituted.freshEvents())
                .isEmpty();
    }

    @Nested
    class CommandsGenerateEvents {

        private static final int PLAYER_HAND_FULL_SIZE = 5;

        @Test
        void creatingGameEmitsGameCreatedEvent() {
            Game game = Game.create("game name", "lovely-dog-23");

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
            Game game = Game.reconstitute(committedEvents);

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
            Game game = Game.reconstitute(committedEvents);

            game.start();

            new EventsAssertion(game.freshEvents())
                    .hasExactly(PlayerDrewActionCard.class, 2 * PLAYER_HAND_FULL_SIZE);

            new EventsAssertion(game.freshEvents())
                    .hasExactly(ActionCardDrawn.class, 2 * PLAYER_HAND_FULL_SIZE);
        }

        @Test
        @Disabled("Until Player can ignore the new player event")
        void playerDiscardsActionCard_PlayerCardDiscarded() {
            Game game = Game.create("IRRELEVANT GAME NAME", "IRRELEVANT HANDLE");
            MemberId firstPlayerMemberId = new MemberId(88L);
            game.join(firstPlayerMemberId, "first player (88)");
            game.join(new MemberId(113L), "second player (113)");
            game.start();
            game = Game.reconstitute(game.freshEvents().toList());

            game.discard(firstPlayerMemberId, ActionCard.PREDICT);

            assertThat(game.freshEvents()).containsExactly(
                    new PlayerDiscardedActionCard(firstPlayerMemberId,
                                                  ActionCard.PREDICT));
        }

        private static Game createFreshGame() {
            return Game.reconstitute(List.of(new GameCreated("IRRELEVANT NAME", "IRRELEVANT HANDLE")));
        }
    }

    @Nested
    class EventsProjectState {

        @Test
        void newGameHasGameNameAndHandle() {
            List<GameEvent> events = List.of(
                    new GameCreated("jitterted", "breezy-cat-85"));
            Game game = Game.reconstitute(events);

            assertThat(game.name())
                    .isEqualTo("jitterted");
            assertThat(game.handle())
                    .isEqualTo("breezy-cat-85");
        }

        @Test
        void playerJoinedResultsInPlayerAddedToGame() {
            List<GameEvent> events = gameCreatedAndTheseEvents(
                    new PlayerJoined(new MemberId(53L), "Member 53 Name"));

            Game game = Game.reconstitute(events);

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

            Game game = Game.reconstitute(events);

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
            Game game = Game.reconstitute(events);

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

        private static List<GameEvent> gameCreatedAndTheseEvents(GameEvent... freshEvents) {
            List<GameEvent> events = new ArrayList<>();
            events.add(new GameCreated("jitterted", "breezy-cat-85"));
            events.addAll(List.of(freshEvents));
            return events;
        }
    }

}