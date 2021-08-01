package com.bol.service.player;

import com.bol.model.dto.PlayerDto;
import com.bol.model.entity.PlayerEntity;

public interface PlayerService {

    Long createPlayer(PlayerDto playerDto);

    void updatePlayer(PlayerDto playerDto);

    void deletePlayer(Long id);

    PlayerEntity getById(Long playerId);


}
