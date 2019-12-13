package com.jitterted.tddgame.domain;

public class Player {
  private final PlayerId playerId;
  private final Hand hand;
  private final InPlay inPlay;

  public Player(PlayerId playerId) {
    this.playerId = playerId;
    hand = new Hand(playerId);
    inPlay = new InPlay();
  }

  public Hand hand() {
    return hand;
  }

  public void drawFrom(Deck deck) {
    hand.add(deck.draw());
  }

  public void fillHandFrom(Deck deck) {
    while (hand.count() < 5) {
      drawFrom(deck);
    }
  }

  public void discard(CardId cardId, Deck deck) {
    Card card = hand.remove(cardId);
    deck.addToDiscardPile(card);
  }

  public InPlay inPlay() {
    return inPlay;
  }

  public void play(CardId cardId) {
    Card card = hand.remove(cardId);
    inPlay.add(card);
  }

  public PlayerId id() {
    return playerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Player player = (Player) o;

    return playerId.equals(player.playerId);
  }

  @Override
  public int hashCode() {
    return playerId.hashCode();
  }
}
