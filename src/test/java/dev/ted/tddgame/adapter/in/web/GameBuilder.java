package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Deck;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Player;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class GameBuilder {

    private final String gameHandle;
    private final String gameName = "Only Game In Progress";
    private GameStore gameStore;
    private final MemberStore memberStore = new MemberStore();
    private Game.GameFactory gameFactory;

    public GameBuilder(String gameHandle) {
        this.gameHandle = gameHandle;
    }

    public static GameBuilder create(String gameHandle) {
        return new GameBuilder(gameHandle);
    }

    public static GameBuilder create() {
        return create("builder-created-game-handle");
    }

    public GameBuilder actionCards(ActionCard... actionCards) {
        Deck.Shuffler<ActionCard> definedCardsShuffler =
                _ -> new ArrayList<>(List.of(actionCards));
        gameFactory = new Game.GameFactory(definedCardsShuffler);
        gameStore = GameStore.createEmpty(gameFactory);

        Game game = gameFactory.create(gameName, gameHandle);
        gameStore.save(game);

        return this;
    }

    public GameBuilder addJoinedPlayer() {
        return this;
    }

    public GameBuilder startGame() {
        return this;
    }

    public GameBuilder discard(ActionCard cardToDiscard) {
        return this;
    }

    public PlayingGameController playingGameController() {
        return null;
    }

    public Game game() {
        return gameStore.findByHandle(gameHandle)
                        .orElseThrow(() -> new RuntimeException("Something has gone terribly wrong, we can't load the Game from our GameStore using the handle: " + gameHandle));
    }

    public Player firstPlayer() {
        return null;
    }

    public Principal firstPlayerPrincipal() {
        return null;
    }

    public MemberStore memberStore() {
        return memberStore;
    }
}
