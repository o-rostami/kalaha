package com.bol.service.player;

import com.bol.exception.NotFoundException;
import com.bol.model.entity.PlayerEntity;
import com.bol.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository repository;

    @Override
    public Long createPlayer(PlayerEntity playerEntity) {
        return repository.save(playerEntity).getId();
    }


    @Override
    public PlayerEntity getById(Long playerId) {
        return repository.findById(playerId).orElseThrow(() -> new NotFoundException("PLAYER.NOT.EXIST"));
    }
}
