package dev.ted.tddgame.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionCardDeck extends Deck<ActionCard> {
    // intended for Production only
    protected ActionCardDeck(List<ActionCard> cards,
                             Deck.Shuffler<ActionCard> shuffler,
                             EventEnqueuer eventEnqueuer) {
        super(cards, shuffler, eventEnqueuer);
    }

    // TODO: for production usage, uses random shuffler
    // TODO: must never accept a list of cards that is empty
    public static Deck<ActionCard> create(List<ActionCard> cards,
                                        EventEnqueuer eventEnqueuer,
                                        Shuffler<ActionCard> shuffler) {
        return new ActionCardDeck(cards, shuffler, eventEnqueuer);
    }

    @Override
    protected DeckEvent createCardDiscardedEvent(ActionCard discardedCard) {
        return new ActionCardDiscarded(discardedCard);
    }

    @Override
    protected DeckEvent createCardDrawnEvent(ActionCard drawnCard) {
        return new ActionCardDrawn(drawnCard);
    }

    @Override
    protected DeckEvent createDeckReplenishedEvent(List<Card> shuffledDiscardedCards) {
        return new ActionCardDeckReplenished(shuffledDiscardedCards);
    }

    // -- FOR TESTS ONLY BELOW --

    protected ActionCardDeck(List<ActionCard> cards,
                             Shuffler<ActionCard> shuffler,
                             List<DeckEvent> deckEventsReceiver) {
        super(cards, shuffler, deckEventsReceiver);
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
}
