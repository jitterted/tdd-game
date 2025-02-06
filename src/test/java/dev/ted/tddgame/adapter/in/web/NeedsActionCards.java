package dev.ted.tddgame.adapter.in.web;

import dev.ted.tddgame.domain.ActionCard;

public interface NeedsActionCards {
    GameBuilder actionCards(ActionCard... actionCards);

    GameBuilder shuffledActionCards();
}
