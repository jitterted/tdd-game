package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.GameView;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerId;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import java.security.Principal;

import static org.assertj.core.api.Assertions.*;

class LobbyTest {

    private static final Principal DUMMY_PRINCIPAL = () -> "Ted";

    @Test
    void noGamesLobbyViewWhenNoGamesExist() {
        Lobby lobby = Lobby.createNull();

        String viewName = lobby.showLobby(DUMMY_PRINCIPAL, new ConcurrentModel());

        assertThat(viewName)
                .isEqualTo("no-game-lobby");
    }

    @Test
    void lobbyViewShowsGamesWhenGamesExist() {
        Lobby lobby = Lobby.createNull(Game.create("game name", "shiny-chrome-12"));

        ConcurrentModel model = new ConcurrentModel();
        String viewName = lobby.showLobby(DUMMY_PRINCIPAL, model);

        assertThat(viewName)
                .isEqualTo("lobby");

        assertThatGameViewsFrom(model)
                .containsExactly(new GameView("game name", "shiny-chrome-12", 0));
    }

    @Test
    void hostNewGameStoresNewGameInGameStore() {
        GameStore gameStore = new GameStore();
        Lobby lobby = Lobby.create(gameStore);

        lobby.hostNewGame(DUMMY_PRINCIPAL, "New Game Name");

        assertThat(gameStore.findAll())
                .hasSize(1);
    }

    @Test
    @Disabled("Depends on PlayerJoinsGame.join() accepting a Game Handle instead of a Game Object")
    void personIsInGameAfterJoinGame() {
        Game game = Game.create("game name", "rush-cat-21");
        Lobby lobby = Lobby.createNull(game);

        Principal principal = () -> "Blue";
        lobby.joinGame(principal, "rush-cat-21");

        assertThat(game.players())
                .hasSize(1)
                .extracting(Player::id)
                .containsExactly(new PlayerId(99L));
    }

    private static ListAssert<GameView> assertThatGameViewsFrom(ConcurrentModel model) {
        return assertThat(model.asMap())
                .extracting("gameViews")
                .asInstanceOf(InstanceOfAssertFactories.list(GameView.class));
    }
}