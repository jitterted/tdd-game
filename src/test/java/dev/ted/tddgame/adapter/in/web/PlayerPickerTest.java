package dev.ted.tddgame.adapter.in.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerPickerTest {

    @Test
    void postToPickPlayerRedirectsToLobby() {
        PlayerPicker playerPicker = new PlayerPicker();

        String redirect = playerPicker.pickPlayer("");

        assertThat(redirect)
                .isEqualTo("redirect:/lobby");
    }
}