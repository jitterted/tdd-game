package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PlayerHandTest {

  private final CardShuffler shuffler = null;
  private final CardFactory cardFactory = new CardFactory();

  @Test
  public void newPlayerDrawsOneCardFromDeckTransfersToHand() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Deck deck = new Deck(shuffler);
    Card theCard = cardFactory.card("The Card");
    deck.addToDrawPile(theCard);

    player.drawFrom(deck);

    assertThat(player.hand().contains(theCard))
      .isTrue();
    assertThat(deck.drawPileSize())
      .isEqualTo(0);
  }

  @Test
  public void playerWithNoCardsFillsHandResultsInFiveCardsInHand() throws Exception {
    Player wietlol = new Player(PlayerId.of(0));
    Deck deck = new Deck(shuffler);
    fillDeck(deck, 5);

    wietlol.fillHandFrom(deck);

    assertThat(wietlol.hand().count())
      .isEqualTo(5);
  }

  @Test
  public void playerWithTwoCardsFillsHandResultsInThreeCardsDrawnFromDeck() throws Exception {
    Player brainw4ashed = new Player(PlayerId.of(0));
    brainw4ashed.hand().add(cardFactory.card("one"));
    brainw4ashed.hand().add(cardFactory.card("two"));
    Deck deck = new Deck(shuffler);
    fillDeck(deck, 7);

    brainw4ashed.fillHandFrom(deck);

    assertThat(deck.drawPileSize())
      .isEqualTo(4);
  }

  @Test
  public void playerWithFullHandWhenFillsHandResultsInNoChangeToDeck() throws Exception {
    Player sheppo162 = new Player(PlayerId.of(0));
    Deck deck = new Deck(shuffler);
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
    Card theCard = cardFactory.card("The Card");
    hand.add(theCard);

    Card removedCard = hand.remove(theCard.id());

    assertThat(hand.isEmpty())
      .isTrue();
    assertThat(removedCard)
      .isEqualTo(theCard);
  }

  @Test
  public void removeCardNotInHandThrowsNotInHandException() throws Exception {
    Player player = new Player(PlayerId.of(0));
    Hand hand = player.hand();
    Card theCard = cardFactory.card("Card Not In Hand");

    assertThatThrownBy(() -> {
      hand.remove(theCard.id());
    })
      .isInstanceOf(CardNotInHandException.class);
  }

  private void fillDeck(Deck deck, int count) {
    for (int i = 0; i < count; i++) {
      deck.addToDrawPile(cardFactory.card(String.valueOf(i)));
    }
  }

}
