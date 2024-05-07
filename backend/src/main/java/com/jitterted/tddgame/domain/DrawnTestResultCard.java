package com.jitterted.tddgame.domain;


public class DrawnTestResultCard {
    private final TestResultCard testResultCard;
    private final Player player;

    public DrawnTestResultCard(TestResultCard testResultCard, Player player) {
        this.testResultCard = testResultCard;
        this.player = player;
    }

    @Override
    public String toString() {
        return player + " drew " + testResultCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DrawnTestResultCard that = (DrawnTestResultCard) o;

        if (!testResultCard.equals(that.testResultCard)) {
            return false;
        }
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        int result = testResultCard.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }

    public Player player() {
        return player;
    }

    public TestResultCard card() {
        return testResultCard;
    }

    public boolean discardableBy(PlayerId playerId) {
        return player.id().equals(playerId);
    }
}
