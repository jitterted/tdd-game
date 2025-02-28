package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.application.GamePlay;
import dev.ted.tddgame.application.GamePlayTest;
import dev.ted.tddgame.application.port.Broadcaster;
import dev.ted.tddgame.application.port.GameStore;
import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.ActionCard;
import dev.ted.tddgame.domain.CardsFactory;
import dev.ted.tddgame.domain.Deck;
import dev.ted.tddgame.domain.Game;
import dev.ted.tddgame.domain.Member;
import dev.ted.tddgame.domain.MemberId;
import dev.ted.tddgame.domain.Player;
import dev.ted.tddgame.domain.TestResultsCard;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GameScenarioBuilder implements NeedsActionCards {

    private final String gameHandle;
    private final String gameName = "Only Game In Progress";
    private String firstPlayerAuthName = "blueauth";
    private MemberId firstPlayerMemberId;
    private GameStore gameStore;
    private final MemberStore memberStore = new MemberStore();
    private Game.GameFactory gameFactory;
    private GamePlay gamePlay;
    private PlayingGameController playingGameController;
    private Broadcaster broadcaster = new GamePlayTest.NoOpDummyBroadcaster();
    private List<ActionCard> configuredActionCardList;
    private Deck.Shuffler<ActionCard> configuredActionCardShuffler;
    private List<TestResultsCard> configuredTestResultsCardList;

    public GameScenarioBuilder(String gameHandle) {
        this.gameHandle = gameHandle;
        configuredActionCardShuffler = new Deck.IdentityShuffler<>();
        configuredTestResultsCardList = new CardsFactory().allTestResultsCards();
    }

    public static NeedsActionCards create(String gameHandle) {
        return new GameScenarioBuilder(gameHandle);
    }

    public static NeedsActionCards create() {
        return create("builder-created-game-handle");
    }

    public static GameScenarioBuilder scenarioPlayerOnPredictTestWillFailToCompile(String gameHandle, TestResultsCard cardToBeDrawn, TestResultsCard testResultsCardRemainingInDrawPile) {
        return create(gameHandle)
                          .actionCards(
                                   ActionCard.PREDICT,
                                   ActionCard.PREDICT,
                                   ActionCard.WRITE_CODE,
                                   ActionCard.LESS_CODE,
                                   ActionCard.LESS_CODE,
                                   ActionCard.REFACTOR
                           )
                          .testResultsCards(cardToBeDrawn,
                                             testResultsCardRemainingInDrawPile)
                          .memberJoinsAsOnlyPlayer()
                          .startGame()
                          .playerOnlyActions(executor -> {
                               executor.discardFirstCardInHand();
                               executor.discardFirstCardInHand();
                               executor.playCard(ActionCard.WRITE_CODE);
                               executor.playCard(ActionCard.PREDICT);
                           });
    }

    public GameScenarioBuilder testResultsCards(TestResultsCard... testResultsCards) {
        configuredTestResultsCardList = Arrays.asList(testResultsCards);
        return this;
    }

    public GameScenarioBuilder actionCards(ActionCard... actionCards) {
        return actionCards(List.of(actionCards));
    }

    public GameScenarioBuilder actionCards(List<ActionCard> actionCardList) {
        this.configuredActionCardList = actionCardList;

        return this;
    }

    @Override
    public GameScenarioBuilder unshuffledActionCards() {
        return actionCards(new CardsFactory().allActionCards());
    }

    public GameScenarioBuilder shuffledActionCards() {
        configuredActionCardShuffler = new Deck.RandomShuffler<>();
        configuredActionCardList = new CardsFactory().allActionCards();

        return this;
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1) {
        return actionCardsHelper(count1, actionCard1);
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2) {
        return actionCardsHelper(count1, actionCard1, count2, actionCard2);
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2,
                                           int count3, ActionCard actionCard3) {
        return actionCardsHelper(count1, actionCard1, count2, actionCard2, count3, actionCard3);
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2,
                                           int count3, ActionCard actionCard3,
                                           int count4, ActionCard actionCard4) {
        return actionCardsHelper(count1, actionCard1, count2, actionCard2,
                                 count3, actionCard3, count4, actionCard4);
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2,
                                           int count3, ActionCard actionCard3,
                                           int count4, ActionCard actionCard4,
                                           int count5, ActionCard actionCard5) {
        return actionCardsHelper(count1, actionCard1, count2, actionCard2,
                                 count3, actionCard3, count4, actionCard4,
                                 count5, actionCard5);
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2,
                                           int count3, ActionCard actionCard3,
                                           int count4, ActionCard actionCard4,
                                           int count5, ActionCard actionCard5,
                                           int count6, ActionCard actionCard6) {
        return actionCardsHelper(count1, actionCard1, count2, actionCard2,
                                 count3, actionCard3, count4, actionCard4,
                                 count5, actionCard5, count6, actionCard6);
    }

    @Override
    public GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                           int count2, ActionCard actionCard2,
                                           int count3, ActionCard actionCard3,
                                           int count4, ActionCard actionCard4,
                                           int count5, ActionCard actionCard5,
                                           int count6, ActionCard actionCard6,
                                           int count7, ActionCard actionCard7
    ) {
        return actionCardsHelper(count1, actionCard1, count2, actionCard2,
                                 count3, actionCard3, count4, actionCard4,
                                 count5, actionCard5, count6, actionCard6,
                                 count7, actionCard7);
    }

    private GameScenarioBuilder actionCardsHelper(Object... args) {
        List<ActionCard> actionCards = new ArrayList<>();
        for (int i = 0; i < args.length; i += 2) {
            actionCards.addAll(Collections.nCopies((Integer) args[i],
                                                   (ActionCard) args[i + 1]));
        }
        return actionCards(actionCards);
    }

    public GameScenarioBuilder memberJoinsAsOnlyPlayer() {
        firstPlayerMemberId = new MemberId(42L);
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
                                                   String memberNickname,
                                                   String authName,
                                                   String playerName) {
        if (memberStore.findById(memberId).isPresent()) {
            throw new IllegalArgumentException("Member already exists");
        }
        // assign only if this is the first member -- don't overwrite existing
        if (firstPlayerMemberId == null) {
            firstPlayerMemberId = memberId;
        }
        Member member = new Member(memberId, memberNickname, authName);
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
        // at this point, if gameStore is null, it means we haven't actually created the Game, etc., so we do it now
        if (gameStore == null) {
            CardsFactory cardsFactory = new CardsFactory(configuredActionCardList,
                                                         configuredTestResultsCardList);
            gameFactory = Game.GameFactory.forTest(configuredActionCardShuffler,
                                                   cardsFactory);
            gameStore = GameStore.createEmpty(gameFactory);

            Game game = gameFactory.create(gameName, gameHandle);
            gameStore.save(game);
        }

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

    MockMvcTester mvcTester() {
        GameStore gameStore = gameStore();
        MemberStore memberStore = memberStore();
        GamePlay gamePlay = new GamePlay(gameStore,
                                         new GamePlayTest.NoOpDummyBroadcaster());
        PlayingGameController playingGameController = new
                PlayingGameController(gameStore, gamePlay, memberStore);
        return MockMvcTester.of(playingGameController);
    }

    public GameScenarioBuilder playerOnlyActions(Consumer<PlayerExecutor> actions) {
        return playerActions(firstPlayerMemberId, actions);
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
