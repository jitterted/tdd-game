package dev.ted.tddgame.adapter.out.websocket;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MultimapTest {

    @Test
    void removeValueRemovesFromAllKeysHoldingThatValue() {
        MessageSendersForPlayers.Multimap<Long, String> longStringMultimap = new MessageSendersForPlayers.Multimap<>();
        longStringMultimap.put(1L, "player1");
        longStringMultimap.put(1L, "player2");
        longStringMultimap.put(2L, "player3");
        longStringMultimap.put(3L, "player2");
        longStringMultimap.put(3L, "player3");

        longStringMultimap.removeValue("player2");

        assertThat(longStringMultimap.get(1L))
                .doesNotContain("player2");
        assertThat(longStringMultimap.get(3L))
                .doesNotContain("player2");
    }
}