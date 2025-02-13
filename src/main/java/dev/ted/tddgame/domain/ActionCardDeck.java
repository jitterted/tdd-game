package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionCardDeck extends Deck<ActionCard> {
    // intended for Production only
    protected ActionCardDeck(List<ActionCard> cards, Deck.Shuffler<ActionCard> shuffler, EventEnqueuer eventEnqueuer) {
        this.discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
        this.eventEnqueuer = eventEnqueuer;
    }

    // intended for tests only
    protected ActionCardDeck(List<ActionCard> cards,
                             Shuffler<ActionCard> shuffler,
                             List<DeckEvent> deckEventsReceiver) {
        this.discardPile = new ArrayList<>(cards);
        this.shuffler = shuffler;
        // TODO: need a different way to create an enqueuer for testing so that we don't need to pass in `this`
        this.eventEnqueuer = new DeckEventEnqueuer<>(this, deckEventsReceiver);
    }

    // TODO: for production usage, uses random shuffler
    // TODO: must never accept a list of cards that is empty
    public static ActionCardDeck create(List<ActionCard> cards,
                                        EventEnqueuer eventEnqueuer,
                                        Shuffler<ActionCard> shuffler) {
        return new ActionCardDeck(cards, shuffler, eventEnqueuer);
    }

    public static ActionCardDeck createForTest(ActionCard... actionCards) {
        return createForTest(Arrays.asList(actionCards));
    }

    public static ActionCardDeck createForTest(List<ActionCard> actionCards) {
        return new ActionCardDeck(actionCards,
                                  new IdentityShuffler<>(),
                                  new ArrayList<>());
    }

    public static ActionCardDeck createForRandomTest(List<ActionCard> actionCards) {
        return new ActionCardDeck(actionCards,
                                  new RandomShuffler<>(),
                                  new ArrayList<>());
    }

    public static ActionCardDeck createForTest(List<DeckEvent> deckEventsReceiver,
                                               ActionCard... actionCards) {
        return new ActionCardDeck(Arrays.asList(actionCards),
                                  new IdentityShuffler<>(),
                                  deckEventsReceiver);
    }

    @Override
    protected ActionCardDrawn createCardDrawnEvent(ActionCard drawnCard) {
        return new ActionCardDrawn(drawnCard);
    }

    @Override
    protected ActionCardDiscarded createCardDiscardedEvent(ActionCard discardedCard) {
        return new ActionCardDiscarded(discardedCard);
    }

}
