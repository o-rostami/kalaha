package com.bol.repository;

import com.bol.model.entity.BoardEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends BaseRepository<BoardEntity, Long> {
}
