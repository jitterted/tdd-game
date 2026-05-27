package dev.ted.tddgame.domain;

import java.util.Arrays;
import java.util.List;

public class ActionCardDeck extends Deck<ActionCard> {
    // intended for Production only
    protected ActionCardDeck(List<ActionCard> cards,
                             Deck.Shuffler<ActionCard> shuffler) {
        super(cards, shuffler);
    }

    // TODO: for production usage, uses random shuffler
    // TODO: must never accept a list of cards that is empty
    public static Deck<ActionCard> create(List<ActionCard> cards,
                                          Shuffler<ActionCard> shuffler) {
        return new ActionCardDeck(cards, shuffler);
    }

    // TODO: ensure this is tested more directly
    @Override
    protected GameEvent createPlayerDrewCard(MemberId memberId, ActionCard drawnCard) {
        return drawnCard.drawnCardEventFor(memberId);
    }

    @Override
    protected GameEvent createDeckReplenishedEvent(List<Card> shuffledDiscardedCards) {
        return new ActionCardDeckReplenished(shuffledDiscardedCards);
    }

    // -- FOR TESTS ONLY BELOW --

    public static ActionCardDeck createForTest(ActionCard... actionCards) {
        return createForTest(Arrays.asList(actionCards));
    }

    public static ActionCardDeck createForTest(List<ActionCard> actionCards) {
        return new ActionCardDeck(actionCards,
                                  new IdentityShuffler<>()
        );
    }

}
