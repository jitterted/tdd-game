package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.adapter.HtmlElement;
import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.Player;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import java.security.Principal;
import java.util.List;

import static dev.ted.tddgame.adapter.HtmlElement.attributes;
import static dev.ted.tddgame.adapter.HtmlElement.text;

@Controller
public class PlayingGame {

    private static final String DISCARD_URI_TEMPLATE_STRING = "/game/{gameHandle}/cards/discard/{cardName}";
    private static final UriTemplate DISCARD_URI_TEMPLATE = new UriTemplate(DISCARD_URI_TEMPLATE_STRING);
    private static final String PLAY_CARD_URI_TEMPLATE_STRING = "/game/{gameHandle}/cards/play/{cardName}";
    private static final UriTemplate PLAY_URI_TEMPLATE = new UriTemplate(PLAY_CARD_URI_TEMPLATE_STRING);
    private final GameStore gameStore;
    private final GamePlay gamePlay;
    private final MemberStore memberStore;

    public PlayingGame(GameStore gameStore, GamePlay gamePlay, MemberStore memberStore) {
        this.gameStore = gameStore;
        this.gamePlay = gamePlay;
        this.memberStore = memberStore;
    }

    @GetMapping("/game/{gameHandle}")
    public String game(Model model,
                       @PathVariable("gameHandle") String gameHandle) {
        List<Player> playerViews = gameStore
                .findByHandle(gameHandle)
                .orElseThrow(() -> new RuntimeException("Game '%s' not found"
                                                                .formatted(gameHandle)))
                .players();
        model.addAttribute("gameView", new GameView(gameHandle, PlayerView.from(playerViews)));
        return "game";
    }

    @PostMapping("/game/{gameHandle}/start-game")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable("gameHandle") String gameHandle) {
        gamePlay.start(gameHandle);
    }

    @GetMapping("/game/{gameHandle}/card-menu/{cardName}")
    @ResponseBody
    public String cardMenu(@PathVariable String gameHandle,
                           @PathVariable String cardName) {
        String playUrlPath = PLAY_URI_TEMPLATE.expand(gameHandle, cardName).getRawPath();
        String discardUrlPath = DISCARD_URI_TEMPLATE.expand(gameHandle, cardName).getRawPath();
        return HtmlElement.swapInnerHtml(
                "dialog",
                HtmlElement.div(
                        "",
                        HtmlElement.button(
                                attributes()
                                        .autofocus()
                                        .hxPost(playUrlPath)
                                        .hxOn("before-request", "document.querySelector('dialog').close()"),
                                text("Play Card into Workspace")
                        )
                ),
                HtmlElement.div(
                        "",
                        HtmlElement.button(
                                attributes()
                                        .hxPost(discardUrlPath)
                                        .hxOn("before-request", "document.querySelector('dialog').close()"),
                                text("Discard Card to Discard Pile")
                        )
                )
        ).render();
    }

    @PostMapping(DISCARD_URI_TEMPLATE_STRING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void discardCardFromHand(Principal principal,
                                    @PathVariable String gameHandle,
                                    @PathVariable String cardName) {
        Member member = memberStore.findByAuthName(principal.getName())
                                   .orElseThrow(() -> new RuntimeException("Member with AuthN username '%s' was not found in the MemberStore".formatted(principal.getName())));
        gamePlay.discard(gameHandle, member.id(), ActionCard.valueOf(cardName));
    }
}
