package dev.ted.tddgame.domain;

public sealed interface TestResultsCardDeckEvent extends DeckEvent
        permits /*TestResultsCardDiscarded,*/ TestResultsCardDrawn/*, TestResultsCardDeckReplenished*/
{
}
