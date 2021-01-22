package com.jitterted.tddgame.domain;

public class Player {
  private final PlayerId playerId;
  private final Hand hand;
  private final InPlay inPlay;
  private User user;

  public Player(PlayerId playerId) {
    this.playerId = playerId;
    hand = new Hand(playerId);
    inPlay = new InPlay();
  }

  public Hand hand() {
    return hand;
  }

  public void drawFrom(Deck<PlayingCard> deck) {
    PlayingCard playingCard = deck.draw();
    playingCard.onDraw(hand, inPlay);
  }

  public void fillHandFrom(Deck<PlayingCard> deck) {
    while (!hand.isFull()) {
      drawFrom(deck);
    }
  }

  public void discardFromHand(CardId cardId, Deck<PlayingCard> deck) {
    PlayingCard playingCard = hand.remove(cardId);
    deck.addToDiscardPile(playingCard);
  }

  public void discardFromInPlay(CardId cardId, Deck<PlayingCard> deck) {
    PlayingCard playingCard = inPlay.remove(cardId);
    deck.addToDiscardPile(playingCard);
  }

  public InPlay inPlay() {
    return inPlay;
  }

  public void play(Game game, CardId cardId) {
    PlayingCard playingCard = hand.remove(cardId);
    playingCard.usage().play(game, this, playingCard);
  }

  public PlayerId id() {
    return playerId;
  }

  public User assignedUser() {
    return user;
  }

  public void assignUser(User user) {
    this.user = user;
  }

  public boolean isAssigned() {
    return user != null;
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

  @Override
  public String toString() {
    return "Player: " + playerId;
  }
}
