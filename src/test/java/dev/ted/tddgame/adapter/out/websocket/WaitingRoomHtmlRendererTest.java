package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.PlayerId;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class WaitingRoomHtmlRendererTest {

    @Test
    void htmlGeneratedCorrectlyForPlayerConnectNotification() {
        String playerName = "Jankom Pog";
        String expectedHtml = """
                              <swap id="notification-container" hx-swap-oob="afterbegin">
                                  <li>[13:45] - Jankom Pog has connected to the game.</li>
                              </swap>
                              """;

        String actualHtml = WaitingRoomHtmlRenderer.forConnectNotification(LocalTime.of(13, 45), playerName);
        assertThat(actualHtml)
                .isEqualTo(expectedHtml);
    }

    @Test
    void htmlGeneratedCorrectlyForOneJoinedAndConnectedPlayer() {
        String playerName = "Murf";
        String expectedHtml = """
                   <li class="flex items-center">
                     <i class="fa-duotone fa-person-circle-check fa-2xl" style="--fa-primary-color: #1fdf00; --fa-secondary-color: #28b900; --fa-secondary-opacity: 0.7;"></i>
                     <p class="ml-3">
                       <strong>Murf</strong>
                     </p>
                   </li>
               """;

        String actualHtml = WaitingRoomHtmlRenderer.forJoinedPlayer(playerName);
        assertThat(actualHtml)
                .isEqualTo(expectedHtml);
    }

    @Test
    void htmlGeneratedCorrectlyForMultipleJoinedPlayers() {
        String firstPlayerName = "Murf";
        Player firstPlayer = new Player(new PlayerId(2L), new MemberId(3L), firstPlayerName);
        String secondPlayerName = "Drednok";
        Player secondPlayer = new Player(new PlayerId(4L), new MemberId(5L), secondPlayerName);
        String expectedHtml = """
               <swap id="joined-players-container" hx-swap-oob="innerHTML">
                   <li class="flex items-center">
                     <i class="fa-duotone fa-person-circle-check fa-2xl" style="--fa-primary-color: #1fdf00; --fa-secondary-color: #28b900; --fa-secondary-opacity: 0.7;"></i>
                     <p class="ml-3">
                       <strong>Murf</strong>
                     </p>
                   </li>
                   <li class="flex items-center">
                     <i class="fa-duotone fa-person-circle-check fa-2xl" style="--fa-primary-color: #1fdf00; --fa-secondary-color: #28b900; --fa-secondary-opacity: 0.7;"></i>
                     <p class="ml-3">
                       <strong>Drednok</strong>
                     </p>
                   </li>
               </swap>
               """;

        String actualHtml = WaitingRoomHtmlRenderer.forJoinedPlayers(List.of(firstPlayer, secondPlayer));
        assertThat(actualHtml)
                .isEqualTo(expectedHtml);
    }


}