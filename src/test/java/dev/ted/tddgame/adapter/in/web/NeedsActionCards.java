package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.ActionCard;

public interface NeedsActionCards {
    GameScenarioBuilder actionCards(ActionCard... actionCards);

    GameScenarioBuilder shuffledActionCards();

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                    int count2, ActionCard actionCard2,
                                    int count3, ActionCard actionCard3,
                                    int count4, ActionCard actionCard4);
}
