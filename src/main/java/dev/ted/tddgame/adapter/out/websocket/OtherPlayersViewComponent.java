package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;

import static dev.ted.tddgame.adapter.out.websocket.HtmlComponent.div;
import static dev.ted.tddgame.adapter.out.websocket.HtmlComponent.swapInnerHtml;
import static dev.ted.tddgame.adapter.out.websocket.HtmlComponent.text;

public class OtherPlayersViewComponent {

    private final Game game;

    public OtherPlayersViewComponent(Game game) {
        this.game = game;
    }

    public HtmlComponent htmlForOtherPlayers() {
        HtmlComponent[] htmlComponents = game.players()
                                             .stream()
                                             .map(OtherPlayersViewComponent::createSwapInnerHtml)
                                             .toArray(HtmlComponent[]::new);
        return new HtmlComponent.Forest(htmlComponents);
    }

    private static HtmlComponent.Swap createSwapInnerHtml(Player player) {
        HtmlComponent nameH2 = text("<h2 class=\"name\">%s</h2>"
                                            .formatted(player.playerName()));
        HtmlComponent divContainer = div("other-player-container",
                                         div("titled-container",
                                             text("Hand"),
                                             new HandViewComponent(player).handDiv()
                                         ));
        return swapInnerHtml("player-id-" + player.id().id(),
                             nameH2,
                             divContainer);
    }

}
