package dev.ted.tddgame.adapter.out.websocket;

import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;

import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.div;
import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.swapInnerHtml;
import static dev.ted.tddgame.adapter.out.websocket.HtmlElement.text;

public class OtherPlayersViewComponent {

    private final Game game;

    public OtherPlayersViewComponent(Game game) {
        this.game = game;
    }

    public HtmlElement htmlForOtherPlayers() {
        HtmlElement[] htmlElements = game.players()
                                         .stream()
                                         .map(this::createSwapInnerHtml)
                                         .toArray(HtmlElement[]::new);
        return new HtmlElement.Forest(htmlElements);
    }

    private HtmlElement createSwapInnerHtml(Player player) {
        HtmlElement nameH2 = text("<h2 class=\"name\">%s</h2>"
                                            .formatted(player.playerName()));
        HtmlElement divContainer = div("other-player-container",
                                       div("titled-container",
                                             text("Hand"),
                                             new HandViewComponent(game.handle(), player)
                                                   .handContainer()
                                         ));
        return swapInnerHtml("player-id-" + player.id().id(),
                             nameH2,
                             divContainer);
    }

}
