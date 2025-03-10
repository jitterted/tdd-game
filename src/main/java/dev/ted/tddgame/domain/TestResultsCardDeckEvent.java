package dev.ted.tddgame.domain;

public sealed interface TestResultsCardDeckEvent extends DeckEvent
        permits TestResultsCardDeckReplenished, TestResultsCardDrawn/*, TestResultsCardDiscarded,*/
{
}
