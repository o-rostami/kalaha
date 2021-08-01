package com.bol.repository;

import com.bol.model.entity.GameEntity;
import com.bol.model.enums.GameStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends BaseRepository<GameEntity, Long> {

    List<GameEntity> findByStatus(GameStatus status);
}
