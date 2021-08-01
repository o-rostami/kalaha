package com.bol.service.board;


import com.bol.model.entity.BoardEntity;
import com.bol.model.enums.DifficultyLevel;

public interface BoardService {

    BoardEntity createBoard(DifficultyLevel difficultyLevel);

    Integer getStones(BoardEntity entity,Integer pitId);

    void setStones(BoardEntity board, Integer pitId, Integer value);
}
