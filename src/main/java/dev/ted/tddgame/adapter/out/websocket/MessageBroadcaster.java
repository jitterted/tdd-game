package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.stream.Collectors;

@Component
public class MessageBroadcaster implements Broadcaster {
    private final MessageSendersForPlayers messageSendersForPlayers;

    public MessageBroadcaster(MessageSendersForPlayers messageSendersForPlayers) {
        this.messageSendersForPlayers = messageSendersForPlayers;
    }

    @Override
    public void announcePlayerConnectedToGame(Game game, Player player) {
        String html =
                WaitingRoomHtmlRenderer.forConnectNotification(LocalTime.now(), player.playerName())
                + WaitingRoomHtmlRenderer.forJoinedPlayers(game.players());
        // create PlayerViewComponent(player, sessionForThatPlayer)
        messageSendersForPlayers.sendToAll(game.handle(), html);
    }

    @Override
    public void clearStartGameModal(Game game) {
        String html = HtmlComponent.swapDelete("modal-container").render();
        messageSendersForPlayers.sendToAll(game.handle(), html);

        // SPIKE TO UPDATE VIEWS OF OTHER PLAYERS
        String otherPlayersHtml = game.players()
                                      .stream()
                                      .map(player -> """
                                                     <div id="player-id-%s" class="other-player-container">
                                                         <h2 class="name">
                                                             Player Named: %s
                                                         </h2>
                                                     </div>
                                                     """.formatted(player.id().id(), player.playerName()))
                                              .collect(Collectors.joining());
        String swap = """
                      <swap id="other-players" hx-swap-oob="innerHTML">
                      %s
                      </swap>
                      """.formatted(otherPlayersHtml);
        messageSendersForPlayers.sendToAll(game.handle(), swap);
    }

    @Override
    public void gameUpdate(Game game) {
        sendCustomHtmlForEachPlayerOf(game);
        // send "other player compact view" to all
    }

    private void sendCustomHtmlForEachPlayerOf(Game game) {
        // FUTURE: we want to iterate through all connected View Components (that encapsulated the MessageSender)
        // so that way we can broadcast updates to Observers who are NOT Players in the game
        for (Player player : game.players()) {
            messageSendersForPlayers.sendTo(
                    game.handle(),
                    player.id(),
                    new PlayerViewComponent(player).generateHtmlAsYou());
        }
    }

}
