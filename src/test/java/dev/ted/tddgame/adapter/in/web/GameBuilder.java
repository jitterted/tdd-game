package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.Deck;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameBuilder implements NeedsActionCards {

    private final String gameHandle;
    private final String gameName = "Only Game In Progress";
    private final String authName = "blueauth";
    private final MemberId memberId = new MemberId(42L);
    private GameStore gameStore;
    private final MemberStore memberStore = new MemberStore();
    private Game.GameFactory gameFactory;
    private GamePlay gamePlay;
    private PlayingGameController playingGameController;

    public GameBuilder(String gameHandle) {
        this.gameHandle = gameHandle;
    }

    public static NeedsActionCards create(String gameHandle) {
        return new GameBuilder(gameHandle);
    }

    public static NeedsActionCards create() {
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

    public GameBuilder shuffledActionCards() {
        gameFactory = new Game.GameFactory();
        gameStore = GameStore.createEmpty(gameFactory);

        Game game = gameFactory.create(gameName, gameHandle);
        gameStore.save(game);

        return this;
    }

    public GameBuilder memberJoinsAsPlayer() {
        return memberJoinsAsPlayer(memberId,
                                   "Default Member Nickname",
                                   authName,
                                   "Default Player Name in game");
    }

    public GameBuilder memberJoinsAsPlayer(MemberId memberId,
                                           String name,
                                           String authName,
                                           String playerName) {
        Member member = new Member(memberId, name, authName);
        memberStore.save(member);
        Game game = game();
        game.join(memberId, playerName);
        gameStore.save(game);

        return this;
    }

    public GameBuilder startGame() {
        gamePlay().start(gameHandle);

        return this;
    }

    public GameBuilder discard(ActionCard cardToDiscard) {
        gamePlay().discard(gameHandle, memberId, cardToDiscard);

        return this;
    }

    public PlayingGameController playingGameController() {
        if (playingGameController == null) {
            playingGameController = new PlayingGameController(gameStore, gamePlay(), memberStore);
        }

        return playingGameController;
    }

    public Game game() {
        return gameStore.findByHandle(gameHandle)
                        .orElseThrow(() -> new RuntimeException("Something has gone terribly wrong, we can't load the Game from our GameStore using the handle: " + gameHandle));
    }

    public Player firstPlayer() {
        return game().players().getFirst();
    }

    public Principal firstPlayerPrincipal() {
        return () -> authName;
    }

    public MemberStore memberStore() {
        return memberStore;
    }

    public GamePlay gamePlay() {
        if (gamePlay == null) {
            Broadcaster dummyBroadcaster = new GamePlayTest.NoOpDummyBroadcaster();
            gamePlay = new GamePlay(gameStore, dummyBroadcaster);
        }

        return gamePlay;
    }

    public GameBuilder playerActions(MemberId memberId, Consumer<PlayerExecutor> actions) {
        PlayerExecutor executor = new PlayerExecutor(gameHandle, memberId, gamePlay(), playerFor(memberId));
        actions.accept(executor);
        return this;
    }

    public String gameHandle() {
        return gameHandle;
    }

    public Player playerFor(MemberId memberId) {
        return game().playerFor(memberId);
    }

    public GameStore gameStore() {
        return gameStore;
    }

    public static class PlayerExecutor {
        private final String gameHandle;
        private final MemberId memberId;
        private final GamePlay gamePlay;
        private final Player player;

        private PlayerExecutor(String gameHandle,
                               MemberId memberId,
                               GamePlay gamePlay,
                               Player player) {
            this.gameHandle = gameHandle;
            this.memberId = memberId;
            this.gamePlay = gamePlay;
            this.player = player;
        }

        public void discard(ActionCard card) {
            gamePlay.discard(gameHandle, memberId, card);
        }

        public void playCard(ActionCard card) {
            gamePlay.playCard(gameHandle, memberId, card);
        }

        public void discardFirstCardInHand() {
            player.hand()
                  .findFirst()
                  .ifPresent(this::discard);
        }
    }

}

