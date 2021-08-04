package com.bol.service.board;

import com.bol.model.entity.BoardEntity;
import com.bol.model.enums.DifficultyLevel;
import com.bol.model.enums.KalahaConstants;
import com.bol.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;

    @Override
    public BoardEntity createBoard(DifficultyLevel difficultyLevel) {

        BoardEntity board = new BoardEntity();
        board.setLeftPitHouseId(0);
        board.setRightPitHouseId(0);

        board.setFirstPitPlayerA(difficultyLevel.getValue());
        board.setSecondPitPlayerA(difficultyLevel.getValue());
        board.setThirdPitPlayerA(difficultyLevel.getValue());
        board.setForthPitPlayerA(difficultyLevel.getValue());
        board.setFifthPitPlayerA(difficultyLevel.getValue());
        board.setSixthPitPlayerA(difficultyLevel.getValue());


        board.setFirstPitPlayerB(difficultyLevel.getValue());
        board.setSecondPitPlayerB(difficultyLevel.getValue());
        board.setThirdPitPlayerB(difficultyLevel.getValue());
        board.setForthPitPlayerB(difficultyLevel.getValue());
        board.setFifthPitPlayerB(difficultyLevel.getValue());
        board.setSixthPitPlayerB(difficultyLevel.getValue());
        return repository.save(board);
    }

    @Override
    public Integer getStones(BoardEntity entity, Integer pitId) {

        if (pitId.equals(KalahaConstants.firstPitPlayerA.getValue())) {
            return entity.getFirstPitPlayerA();
        } else if (pitId.equals(KalahaConstants.secondPitPlayerA.getValue())) {
            return entity.getSecondPitPlayerA();
        } else if (pitId.equals(KalahaConstants.thirdPitPlayerA.getValue())) {
            return entity.getThirdPitPlayerA();
        } else if (pitId.equals(KalahaConstants.forthPitPlayerA.getValue())) {
            return entity.getForthPitPlayerA();
        } else if (pitId.equals(KalahaConstants.fifthPitPlayerA.getValue())) {
            return entity.getFifthPitPlayerA();
        } else if (pitId.equals(KalahaConstants.sixthPitPlayerA.getValue())) {
            return entity.getSixthPitPlayerA();
        } else if (pitId.equals(KalahaConstants.leftPitHouseId.getValue())) {
            return entity.getLeftPitHouseId();
        } else if (pitId.equals(KalahaConstants.firstPitPlayerB.getValue())) {
            return entity.getFirstPitPlayerB();
        } else if (pitId.equals(KalahaConstants.secondPitPlayerB.getValue())) {
            return entity.getSecondPitPlayerB();
        } else if (pitId.equals(KalahaConstants.thirdPitPlayerB.getValue())) {
            return entity.getThirdPitPlayerB();
        } else if (pitId.equals(KalahaConstants.forthPitPlayerB.getValue())) {
            return entity.getForthPitPlayerB();
        } else if (pitId.equals(KalahaConstants.fifthPitPlayerB.getValue())) {
            return entity.getFifthPitPlayerB();
        } else if (pitId.equals(KalahaConstants.sixthPitPlayerB.getValue())) {
            return entity.getSixthPitPlayerB();
        } else if (pitId.equals(KalahaConstants.rightPitHouseId.getValue())) {
            return entity.getRightPitHouseId();
        }
        return 0;
    }

    @Override
    public void setStones(BoardEntity entity, Integer pitId, Integer value) {

        if (pitId.equals(KalahaConstants.firstPitPlayerA.getValue())) {
            entity.setFirstPitPlayerA(value);
        } else if (pitId.equals(KalahaConstants.secondPitPlayerA.getValue())) {
            entity.setSecondPitPlayerA(value);
        } else if (pitId.equals(KalahaConstants.thirdPitPlayerA.getValue())) {
            entity.setThirdPitPlayerA(value);
        } else if (pitId.equals(KalahaConstants.forthPitPlayerA.getValue())) {
            entity.setForthPitPlayerA(value);
        } else if (pitId.equals(KalahaConstants.fifthPitPlayerA.getValue())) {
            entity.setFifthPitPlayerA(value);
        } else if (pitId.equals(KalahaConstants.sixthPitPlayerA.getValue())) {
            entity.setSixthPitPlayerA(value);
        } else if (pitId.equals(KalahaConstants.leftPitHouseId.getValue())) {
            entity.setLeftPitHouseId(value);
        } else if (pitId.equals(KalahaConstants.firstPitPlayerB.getValue())) {
            entity.setFirstPitPlayerB(value);
        } else if (pitId.equals(KalahaConstants.secondPitPlayerB.getValue())) {
            entity.setSecondPitPlayerB(value);
        } else if (pitId.equals(KalahaConstants.thirdPitPlayerB.getValue())) {
            entity.setThirdPitPlayerB(value);
        } else if (pitId.equals(KalahaConstants.forthPitPlayerB.getValue())) {
            entity.setForthPitPlayerB(value);
        } else if (pitId.equals(KalahaConstants.fifthPitPlayerB.getValue())) {
            entity.setFifthPitPlayerB(value);
        } else if (pitId.equals(KalahaConstants.sixthPitPlayerB.getValue())) {
            entity.setSixthPitPlayerB(value);
        } else if (pitId.equals(KalahaConstants.rightPitHouseId.getValue())) {
            entity.setRightPitHouseId(value);
        }
    }

}
