package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.GameView;
import org.assertj.core.api.InstanceOfAssertFactories;
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
        Lobby lobby = Lobby.createNull(new GameView());

        ConcurrentModel model = new ConcurrentModel();
        String viewName = lobby.showLobby(DUMMY_PRINCIPAL, model);

        assertThat(viewName)
                .isEqualTo("lobby");

        assertThat(model.asMap())
                .extracting("gameViews")
                .asInstanceOf(InstanceOfAssertFactories.list(GameView.class))
                .containsExactly(new GameView());
    }

    @Test
    @Disabled("Until showLobby returns existing games to join")
    void newGameWithHandleCreatedUponHostNewGame() {
        Lobby lobby = Lobby.createNull();

        lobby.hostNewGame(DUMMY_PRINCIPAL, "game name");


    }
}