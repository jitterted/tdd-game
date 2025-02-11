package dev.ted.tddgame.domain;

public final class RegularCard extends ActionCard {
    RegularCard(String title, String name) {
        super(title, name);
    }

    @Override
    PlayerEvent drawnCardEventFor(MemberId memberId) {
        return new PlayerDrewActionCard(memberId, this);
    }
}
