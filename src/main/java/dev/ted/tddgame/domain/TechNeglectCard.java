package dev.ted.tddgame.domain;

public final class TechNeglectCard extends ActionCard {
    TechNeglectCard(String title, String name) {
        super(title, name);
    }

    @Override
    PlayerEvent playedCardEventFor(MemberId memberId) {
        return new PlayerDrewTechNeglectCard(memberId, this);
    }
}
