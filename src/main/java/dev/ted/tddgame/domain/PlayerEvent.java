package dev.ted.tddgame.domain;

public sealed interface PlayerEvent extends GameEvent
         permits PlayerDrewActionCard {

}
