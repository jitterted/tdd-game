package dev.ted.tddgame.application.port;

import dev.ted.tddgame.domain.Member;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemberStore {

    private final Map<String, Member> authNameToMemberMap = new HashMap<>();

    public void save(Member member) {
        authNameToMemberMap.put(member.authName(), member);
    }

    public Optional<Member> findByAuthName(String authName) {
        return Optional.ofNullable(authNameToMemberMap.get(authName));
    }
}
