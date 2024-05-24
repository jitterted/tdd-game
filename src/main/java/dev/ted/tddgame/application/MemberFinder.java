package dev.ted.tddgame.application;

import dev.ted.tddgame.application.port.MemberStore;
import dev.ted.tddgame.domain.Member;

public class MemberFinder {
    private final MemberStore memberStore;

    public MemberFinder(MemberStore memberStore) {
        this.memberStore = memberStore;
    }

    public static MemberFinder createNull(Member member) {
        MemberStore memberStore = new MemberStore();
        memberStore.save(member);
        return new MemberFinder(memberStore);
    }

    public Member byUsername(String username) {
        return memberStore.findByAuthName(username)
                          .orElseThrow(() -> new RuntimeException("Member not found with authName: " + username));
    }
}
