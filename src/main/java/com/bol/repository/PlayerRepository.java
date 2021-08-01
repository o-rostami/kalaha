package com.bol.repository;

import com.bol.model.entity.PlayerEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends BaseRepository<PlayerEntity, Long> {
}
