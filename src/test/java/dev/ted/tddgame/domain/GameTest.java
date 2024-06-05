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
        void startGameEmitsGameStartedAndPlayerDrewCardEvents() {
            Game game = Game.reconstitute(List.of(
                    new GameCreated("IRRELEVANT NAME", "IRRELEVANT HANDLE"),
                    new PlayerJoined(new MemberId(1L), "player 1")));

            game.start();

            assertThat(game.freshEvents())
                    .containsExactly(
                            new GameStarted(),
                            // player 1 draws 5 cards (that's the "full hand")
                            new PlayerDrewActionCard(new MemberId(1L), ActionCard.PREDICT),
                            new PlayerDrewActionCard(new MemberId(1L), ActionCard.LESS_CODE),
                            new PlayerDrewActionCard(new MemberId(1L), ActionCard.LESS_CODE),
                            new PlayerDrewActionCard(new MemberId(1L), ActionCard.WRITE_CODE),
                            new PlayerDrewActionCard(new MemberId(1L), ActionCard.PREDICT)
                    );
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
            List<GameEvent> events = gameCreatedAndTheEvents(
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
        void playerDrewCardResultsInPlayerHavingCardDrawn() {
            List<GameEvent> events = gameCreatedAndTheEvents(
                    new PlayerJoined(new MemberId(53L), "Member 53 Name"),
                    new PlayerDrewActionCard(new MemberId(53L), ActionCard.PREDICT));
            Game game = Game.reconstitute(events);

            Player player = game.playerFor(new MemberId(53L));
            assertThat(player.hand())
                    .containsExactly(ActionCard.PREDICT);
        }

        private static List<GameEvent> gameCreatedAndTheEvents(GameEvent... freshEvents) {
            List<GameEvent> events = new ArrayList<>();
            events.add(new GameCreated("jitterted", "breezy-cat-85"));
            events.addAll(List.of(freshEvents));
            return events;
        }
    }

}