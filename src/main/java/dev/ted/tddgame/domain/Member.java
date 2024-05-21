package dev.ted.tddgame.domain;

public class Member {
    private final MemberId memberId;

    public Member(MemberId memberId) {
        this.memberId = memberId;
    }

    public MemberId id() {
        return memberId;
    }
}
