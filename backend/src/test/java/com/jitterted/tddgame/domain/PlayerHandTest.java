package com.jitterted.tddgame.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerHandTest {

  private final CardShuffler shuffler = null;

  @Test
  public void newPlayerHasEmptyHand() throws Exception {
    Player player = new Player();

    assertThat(player.hand().isEmpty())
      .isTrue();
  }

  @Test
  public void newPlayerDrawsOneCardFromDeckTransfersToHand() throws Exception {
    Player player = new Player();
    Deck deck = new Deck(shuffler);
    Card theCard = new Card(1, "The Card");
    deck.addToDrawPile(theCard);

    player.drawFrom(deck);

    assertThat(player.hand().contains(theCard))
      .isTrue();
    assertThat(deck.drawPileSize())
      .isEqualTo(0);
  }

  @Test
  public void playerWithNoCardsFillsHandResultsInFiveCardsInHand() throws Exception {
    Player wietlol = new Player();
    Deck deck = new Deck(shuffler);
    fillDeck(deck, 5);

    wietlol.fillHandFrom(deck);

    assertThat(wietlol.hand().count())
      .isEqualTo(5);
  }

  @Test
  public void playerWithTwoCardsFillsHandResultsInThreeCardsDrawnFromDeck() throws Exception {
    Player brainw4ashed = new Player();
    brainw4ashed.hand().add(new Card(2, "one"));
    brainw4ashed.hand().add(new Card(3, "two"));
    Deck deck = new Deck(shuffler);
    fillDeck(deck, 7);

    brainw4ashed.fillHandFrom(deck);

    assertThat(deck.drawPileSize())
      .isEqualTo(4);
  }

  @Test
  public void playerWithFullHandWhenFillsHandResultsInNoChangeToDeck() throws Exception {
    Player sheppo162 = new Player();
    Deck deck = new Deck(shuffler);
    fillDeck(deck, 9);
    sheppo162.fillHandFrom(deck);

    sheppo162.fillHandFrom(deck);

    assertThat(deck.drawPileSize())
      .isEqualTo(4);
    assertThat(sheppo162.hand().count())
      .isEqualTo(5);
  }

  private void fillDeck(Deck deck, int count) {
    for (int i = 0; i < count; i++) {
      deck.addToDrawPile(new Card(i, String.valueOf(i)));
    }
  }
}
