package com.bol.service.player;

import com.bol.exception.NotFoundException;
import com.bol.model.dto.PlayerDto;
import com.bol.model.entity.PlayerEntity;
import com.bol.model.mapper.PlayerMapper;
import com.bol.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository repository;
    private PlayerMapper mapper;

    @Override
    public Long createPlayer(PlayerDto playerDto) {
        return repository.save(mapper.dtoToEntity(playerDto)).getId();
    }

    @Override
    public void updatePlayer(PlayerDto playerDto) {
        repository.save(mapper.dtoToEntity(playerDto));
    }

    @Override
    public void deletePlayer(Long playerId) {
        repository.deleteById(playerId);
    }

    @Override
    public PlayerEntity getById(Long playerId) {
        return repository.findById(playerId).orElseThrow(() -> new NotFoundException("PLAYER.NOT.EXIST"));
    }
}
