package com.bol.service.board;


import com.bol.model.entity.BoardEntity;
import com.bol.model.enums.DifficultyLevel;

/**
 * A <i>BoardService</i>. This interface has responsibility to create and
 * set and get stones of specified pit number.<p>
 * The <tt>BoardService</tt> interface provides three methods for controlling the board
 * and implemented by <tt>BoardServiceImpl</tt> class.<p>
 *
 * @author Omid Rostami
 */

public interface BoardService {

    /**
     * Returns the created board based on the specified difficulty level.
     *
     * @param difficultyLevel the difficulty Level includes EASY, MEDIUM, HARD
     * @return the board entity just created.
     */

    BoardEntity createBoard(DifficultyLevel difficultyLevel);

    /**
     * Returns stone number of board based on the pit id.
     *
     * @param entity the specified board
     * @param pitId  the pit id of board
     * @return stone number of board.
     */

    Integer getStones(BoardEntity entity, Integer pitId);

    /**
     * Set numbers of stones for specified pit id
     *
     * @param entity the specified board
     * @param pitId  the pit id of board
     * @param value  the number of stones which will be assigned to the pit
     */

    void setStones(BoardEntity board, Integer pitId, Integer value);
}
