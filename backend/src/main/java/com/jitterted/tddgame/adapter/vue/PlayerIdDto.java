package com.jitterted.tddgame.adapter.vue;

import com.jitterted.tddgame.domain.PlayerId;

public class PlayerIdDto {
    private String playerId;

    public static PlayerIdDto from(PlayerId id) {
        PlayerIdDto playerIdDto = new PlayerIdDto();
        playerIdDto.setPlayerId(String.valueOf(id.getId()));
        return playerIdDto;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
