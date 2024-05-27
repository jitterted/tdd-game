package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Player;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class WaitingRoomHtmlRenderer {
    static String forConnectNotification(LocalTime now, String playerName) {
        return """
               <swap id="notification-container" hx-swap-oob="afterbegin">
                   <li>[%s] - %s has connected to the game.</li>
               </swap>
               """.formatted(DateTimeFormatter.ofPattern("HH:mm").format(now),
                             playerName);
    }

    static String forJoinedPlayers(List<Player> players) {
        return """
               <swap id="joined-players-container" hx-swap-oob="innerHTML">
               """
               +
               players.stream()
                      .map(player -> forJoinedPlayer(player.playerName()))
                      .reduce("", (html, next) -> html + next)
               +
               """
               </swap>
               """;
    }

    static String forJoinedPlayer(String playerName) {
        return """
                   <li class="flex items-center">
                     <i class="fa-duotone fa-person-circle-check fa-2xl" style="--fa-primary-color: #1fdf00; --fa-secondary-color: #28b900; --fa-secondary-opacity: 0.7;"></i>
                     <p class="ml-3">
                       <strong>%s</strong>
                     </p>
                   </li>
               """.formatted(playerName);
    }
}
