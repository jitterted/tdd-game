package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PlayerHandTest {

  private final CardFactory cardFactory = new CardFactory();

  @Test
  public void newPlayerDrawsOneCardFromDeckTransfersToHand() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Deck<PlayingCard> deck = new Deck<>(new DummyCardShuffler<>());
    PlayingCard thePlayingCard = cardFactory.playingCard("The Card", Usage.SELF);
    deck.addToDrawPile(thePlayingCard);

    player.drawFrom(deck);

    assertThat(player.hand().contains(thePlayingCard))
      .isTrue();
    assertThat(deck.drawPileSize())
      .isEqualTo(0);
  }

  @Test
  public void playerWithNoCardsFillsHandResultsInFiveCardsInHand() throws Exception {
    Player wietlol = new Player(PlayerId.of(0));
    Deck<PlayingCard> deck = new Deck<>(new DummyCardShuffler<>());
    fillDeck(deck, 5);

    wietlol.fillHandFrom(deck);

    assertThat(wietlol.hand().count())
      .isEqualTo(5);
  }

  @Test
  public void playerWithTwoCardsFillsHandResultsInThreeCardsDrawnFromDeck() throws Exception {
    Player brainw4ashed = new Player(PlayerId.of(0));
    brainw4ashed.hand().add(cardFactory.playingCard("one", Usage.SELF));
    brainw4ashed.hand().add(cardFactory.playingCard("two", Usage.SELF));
    Deck<PlayingCard> deck = new Deck<>(new DummyCardShuffler<>());
    fillDeck(deck, 7);

    brainw4ashed.fillHandFrom(deck);

    assertThat(deck.drawPileSize())
      .isEqualTo(4);
  }

  @Test
  public void playerWithFullHandWhenFillsHandResultsInNoChangeToDeck() throws Exception {
    Player sheppo162 = new Player(PlayerId.of(0));
    Deck<PlayingCard> deck = new Deck<>(new DummyCardShuffler<>());
    fillDeck(deck, 9);
    sheppo162.fillHandFrom(deck);

    sheppo162.fillHandFrom(deck);

    assertThat(deck.drawPileSize())
      .isEqualTo(4);
    assertThat(sheppo162.hand().count())
      .isEqualTo(5);
  }

  @Test
  public void removeCardFromHandMeansCardNoLongerInHand() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Hand hand = player.hand();
    PlayingCard thePlayingCard = cardFactory.playingCard("The Card", Usage.SELF);
    hand.add(thePlayingCard);

    PlayingCard removedPlayingCard = hand.remove(thePlayingCard.id());

    assertThat(hand.isEmpty())
      .isTrue();
    assertThat(removedPlayingCard)
      .isEqualTo(thePlayingCard);
  }

  @Test
  public void removeCardNotInHandThrowsNotInHandException() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Hand hand = player.hand();
    PlayingCard thePlayingCard = cardFactory.playingCard("Card Not In Hand", Usage.SELF);

    assertThatThrownBy(() -> {
      hand.remove(thePlayingCard.id());
    })
      .isInstanceOf(CardNotInHandException.class);
  }

  private void fillDeck(Deck<PlayingCard> deck, int count) {
    for (int i = 0; i < count; i++) {
      deck.addToDrawPile(cardFactory.playingCard(String.valueOf(i), Usage.SELF));
    }
  }

}
