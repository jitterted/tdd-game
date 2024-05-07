package com.jitterted.tddgame.domain;

import com.jitterted.tddgame.domain.port.GameStateChannel;

import java.util.List;

public class GameFactory {

    private final DeckFactory deckFactory;
    private final PlayerFactory playerFactory;
    private GameStateChannel gameStateChannel;

    public GameFactory() {
        this(new DefaultDeckFactory(new CardFactory()), new PlayerFactory());
    }

    public GameFactory(DeckFactory deckFactory, PlayerFactory playerFactory) {
        this.deckFactory = deckFactory;
        this.playerFactory = playerFactory;
    }

    public GameFactory(DeckFactory deckFactory, PlayerFactory playerFactory, GameStateChannel gameStateChannel) {
        this(deckFactory, playerFactory);
        this.gameStateChannel = gameStateChannel;
    }

    public Game createTwoPlayerGame() {
        List<Player> playerList = playerFactory.createTwoPlayers();

        if (gameStateChannel == null) { // YOIKS!
            return new Game(
                    playerList,
                    deckFactory.createPlayingCardDeck(),
                    deckFactory.createTestResultCardDeck()
            );
        } else {
            return new Game(
                    playerList,
                    deckFactory.createPlayingCardDeck(),
                    deckFactory.createTestResultCardDeck(),
                    gameStateChannel
            );
        }
    }
}
