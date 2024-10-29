package dev.ted.tddgame.domain;

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

        @Test
        void creatingGameEmitsGameCreatedEvent() {
            Game game = Game.create("game name", "lovely-dog-23");

            Stream<GameEvent> events = game.freshEvents();

            assertThat(events)
                    .containsExactly(new GameCreated("game name", "lovely-dog-23"));
        }

        @Test
        void playerJoiningEmitsPlayerJoinedEvent() {
            Game game = createFreshGame();

            game.join(new MemberId(88L), "player name");

            assertThat(game.freshEvents())
                    .containsExactly(new PlayerJoined(new MemberId(88L), "player name"));
        }

        @Test
        void startGameEmitsGameStarted_DeckCreated_PlayerDrawCards_Events() {
            MemberId memberId = new MemberId(1L);
            List<GameEvent> committedEvents = List.of(
                    new GameCreated("IRRELEVANT NAME", "IRRELEVANT HANDLE"),
                    new PlayerJoined(memberId, "player 1"));
            Game game = Game.configureForTest(
                    committedEvents,
                    ActionCard.PREDICT,
                    ActionCard.LESS_CODE,
                    ActionCard.LESS_CODE,
                    ActionCard.WRITE_CODE,
                    ActionCard.PREDICT
            );

            game.start();


            assertThat(game.freshEvents())
                    .hasExactlyElementsOfTypes(
                            GameStarted.class,
                            ActionCardDeckCreated.class,
                            DeckReplenished.class,
                            DeckCardDrawn.class,
                            PlayerDrewActionCard.class,
                            DeckCardDrawn.class,
                            PlayerDrewActionCard.class,
                            DeckCardDrawn.class,
                            PlayerDrewActionCard.class,
                            DeckCardDrawn.class,
                            PlayerDrewActionCard.class,
                            DeckCardDrawn.class,
                            PlayerDrewActionCard.class);
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

            assertThat(game.actionCardDeck().drawPile())
                    .isEmpty();
            assertThat(game.actionCardDeck().discardPile())
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
                    new DeckReplenished<>(List.of(
                            ActionCard.PREDICT,
                            ActionCard.REFACTOR)),
                    new DeckCardDrawn<>(ActionCard.PREDICT),
                    new PlayerDrewActionCard(new MemberId(53L), ActionCard.PREDICT));
            Game game = Game.reconstitute(events);

            Player player = game.playerFor(new MemberId(53L));
            assertThat(player.hand())
                    .as("Hand had unexpected cards")
                    .containsExactly(ActionCard.PREDICT);
            assertThat(game.actionCardDeck().drawPile())
                    .as("draw pile was not as expected")
                    .containsExactly(ActionCard.REFACTOR);
            assertThat(game.actionCardDeck().discardPile())
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