package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.ActionCard;

import java.util.List;

public interface NeedsActionCards {
    GameScenarioBuilder actionCards(ActionCard... actionCards);

    GameScenarioBuilder shuffledActionCards();

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1);

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                   int count2, ActionCard actionCard2);

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                   int count2, ActionCard actionCard2,
                                   int count3, ActionCard actionCard3);

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                    int count2, ActionCard actionCard2,
                                    int count3, ActionCard actionCard3,
                                    int count4, ActionCard actionCard4);

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                    int count2, ActionCard actionCard2,
                                    int count3, ActionCard actionCard3,
                                    int count4, ActionCard actionCard4,
                                    int count5, ActionCard actionCard5);

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                    int count2, ActionCard actionCard2,
                                    int count3, ActionCard actionCard3,
                                    int count4, ActionCard actionCard4,
                                    int count5, ActionCard actionCard5,
                                    int count6, ActionCard actionCard6
    );

    GameScenarioBuilder actionCards(int count1, ActionCard actionCard1,
                                    int count2, ActionCard actionCard2,
                                    int count3, ActionCard actionCard3,
                                    int count4, ActionCard actionCard4,
                                    int count5, ActionCard actionCard5,
                                    int count6, ActionCard actionCard6,
                                    int count7, ActionCard actionCard7
    );

    GameScenarioBuilder actionCards(List<ActionCard> actionCards);

    GameScenarioBuilder unshuffledActionCards();
}
