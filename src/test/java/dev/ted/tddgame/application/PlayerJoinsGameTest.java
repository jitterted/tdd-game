package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class PlayerJoinsGameTest {

    @Test
    void membersCanJoinNewGame() {
        Game game = Game.create("new", "new");

        assertThat(game.canJoin(new MemberId(42L)))
                .isTrue();
    }

    @Test
    void memberJoinsExistingGameThenAddedAsPlayer() {
        Fixture fixture = createFixture();
        Member member = new Member(new MemberId(27L));

        fixture.playerJoinsGame().join(member.id(), fixture.game().handle());

        assertThat(fixture.game().players())
                .hasSize(1)
                .extracting(Player::memberId)
                .containsExactly(new MemberId(27L));
    }

    @Test
    void twoMembersJoinExistingGameThenBothAddedAsDifferentPlayers() {
        Fixture fixture = createFixture();
        MemberId firstMember = new MemberId(7L);
        MemberId secondMember = new MemberId(8L);

        fixture.playerJoinsGame().join(firstMember, fixture.game().handle());
        fixture.playerJoinsGame().join(secondMember, fixture.game().handle());

        assertThat(fixture.game().players())
                .map(Player::memberId)
                .containsExactly(firstMember, secondMember);
    }

    @Test
    void memberJoinsIsNotAddedAsNewPlayerWhenAlreadyPlayerInGame() {
        Fixture fixture = createFixture();
        MemberId memberId = new MemberId(7L);
        fixture.playerJoinsGame().join(memberId, fixture.game().handle());
        fixture.playerJoinsGame().join(new MemberId(8L), fixture.game().handle());

        fixture.playerJoinsGame().join(memberId, fixture.game().handle());

        assertThat(fixture.game().players())
                .as("Expected 2 players, as first member rejoined")
                .hasSize(2);
    }

    @Test
    void newMemberCanNotJoinGameWith_4_Players() {
        Game game = gameWith4Players(7L, 9L, 11L, 13L);

        assertThat(game.canJoin(new MemberId(42L)))
                .isFalse();
    }

    @Test
    void noExceptionThrownIfPlayerAlreadyInGame() {
        long existingMemberId = 11L;
        Game game = gameWith4Players(7L, 9L, existingMemberId, 13L);

        assertThatNoException()
                .isThrownBy(() -> game.join(new MemberId(existingMemberId)));
    }

    @Nested
    class UnhappyScenarios {

        @Test
        void exceptionThrownIfNewPlayerCanNotJoinGame() {
            Game game = gameWith4Players(7L, 9L, 11L, 13L);

            assertThatIllegalStateException()
                    .isThrownBy(() -> game.join(new MemberId(18L)))
                    .withMessage("Game is full (Member IDs: [7, 9, 11, 13]), so MemberId[id=18] cannot join.");
        }

        @Test
        void exceptionThrownIfGameHandleIsNotFoundInGameStore() {
            PlayerJoinsGame playerJoinsGame = PlayerJoinsGame.createNull();

            assertThatIllegalArgumentException()
                    .isThrownBy(() -> playerJoinsGame.join(new MemberId(42L), "non-existing-game-handle"))
                    .withMessage("Game with handle 'non-existing-game-handle' was not found in the GameStore.");
        }
    }
    // -- ENCAPSULATED SETUP

    private Game gameWith4Players(Long... memberIds) {
        Fixture fixture = createFixture();

        Stream.of(memberIds)
              .map(MemberId::new)
              .forEach(memberId -> fixture.playerJoinsGame()
                                          .join(memberId, fixture.game().handle()));

        return fixture.game();
    }

    private static Fixture createFixture() {
        GameStore gameStore = new GameStore();
        PlayerJoinsGame playerJoinsGame = new PlayerJoinsGame(gameStore);
        Game game = new GameCreator(gameStore).createNewGame("TDD Game");
        return new Fixture(playerJoinsGame, game);
    }

    private record Fixture(PlayerJoinsGame playerJoinsGame, Game game) {
    }


}