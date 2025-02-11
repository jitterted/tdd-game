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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GameScenarioBuilder implements NeedsActionCards {

    private final String gameHandle;
    private final String gameName = "Only Game In Progress";
    private String firstPlayerAuthName = "blueauth";
    private final MemberId firstPlayerMemberId = new MemberId(42L);
    private GameStore gameStore;
    private final MemberStore memberStore = new MemberStore();
    private Game.GameFactory gameFactory;
    private GamePlay gamePlay;
    private PlayingGameController playingGameController;
    private Broadcaster broadcaster = new GamePlayTest.NoOpDummyBroadcaster();

    public GameScenarioBuilder(String gameHandle) {
        this.gameHandle = gameHandle;
    }

    public static NeedsActionCards create(String gameHandle) {
        return new GameScenarioBuilder(gameHandle);
    }

    public static NeedsActionCards create() {
        return create("builder-created-game-handle");
    }

    public GameScenarioBuilder actionCards(ActionCard... actionCards) {
        return actionCards(List.of(actionCards));
    }

    private GameScenarioBuilder actionCards(List<ActionCard> actionCardList) {
        Deck.Shuffler<ActionCard> definedCardsShuffler =
                _ -> new ArrayList<>(actionCardList);
        gameFactory = new Game.GameFactory(definedCardsShuffler);
        gameStore = GameStore.createEmpty(gameFactory);

        Game game = gameFactory.create(gameName, gameHandle);
        gameStore.save(game);

        return this;
    }

    public GameScenarioBuilder shuffledActionCards() {
        gameFactory = new Game.GameFactory();
        gameStore = GameStore.createEmpty(gameFactory);

        Game game = gameFactory.create(gameName, gameHandle);
        gameStore.save(game);

        return this;
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2,
                                           int count3, ActionCard actionCard3,
                                           int count4, ActionCard actionCard4
    ) {
        List<ActionCard> actionCards = new ArrayList<>();
        actionCards.addAll(Collections.nCopies(count1, actionCard1));
        actionCards.addAll(Collections.nCopies(count2, actionCard2));
        actionCards.addAll(Collections.nCopies(count3, actionCard3));
        actionCards.addAll(Collections.nCopies(count4, actionCard4));

        return actionCards(actionCards);
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2,
                                           int count3, ActionCard actionCard3,
                                           int count4, ActionCard actionCard4,
                                           int count5, ActionCard actionCard5,
                                           int count6, ActionCard actionCard6
    ) {
        List<ActionCard> actionCards = new ArrayList<>();
        actionCards.addAll(Collections.nCopies(count1, actionCard1));
        actionCards.addAll(Collections.nCopies(count2, actionCard2));
        actionCards.addAll(Collections.nCopies(count3, actionCard3));
        actionCards.addAll(Collections.nCopies(count4, actionCard4));
        actionCards.addAll(Collections.nCopies(count5, actionCard5));
        actionCards.addAll(Collections.nCopies(count6, actionCard6));

        return actionCards(actionCards);
    }

    public GameScenarioBuilder memberJoinsAsOnlyPlayer() {
        return memberJoinsAsPlayer(firstPlayerMemberId,
                                   "Default Member Nickname",
                                   firstPlayerAuthName,
                                   "Default Player Name in game");
    }

    public GameScenarioBuilder memberJoinsAsPlayer(MemberId memberId) {
        return memberJoinsAsPlayer(memberId,
                                   "Default Name for " + memberId,
                                   "defaultAuthNameFor" + memberId.id(),
                                   "Default Player Name for " + memberId
        );
    }

    public GameScenarioBuilder memberJoinsAsPlayer(MemberId memberId,
                                                   String name,
                                                   String authName,
                                                   String playerName) {
        if (memberStore.findById(memberId).isPresent()) {
            throw new IllegalArgumentException("Member already exists");
        }
        Member member = new Member(memberId, name, authName);
        memberStore.save(member);
        Game game = game();
        if (game.players().isEmpty()) {
            firstPlayerAuthName = authName;
        }
        game.join(memberId, playerName);
        gameStore.save(game);

        return this;
    }

    public GameScenarioBuilder startGame() {
        gamePlay().start(gameHandle);

        return this;
    }

    public GameScenarioBuilder discard(ActionCard cardToDiscard) {
        gamePlay().discard(gameHandle, firstPlayerMemberId, cardToDiscard);

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
        return () -> firstPlayerAuthName;
    }

    public MemberStore memberStore() {
        return memberStore;
    }

    public GamePlay gamePlay() {
        if (gamePlay == null) {
            gamePlay = new GamePlay(gameStore, broadcaster);
        }

        return gamePlay;
    }

    public GameScenarioBuilder playerActions(MemberId memberId, Consumer<PlayerExecutor> actions) {
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

    public GameScenarioBuilder withBroadcaster(Broadcaster broadcaster) {
        this.broadcaster = broadcaster;
        return this;
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

