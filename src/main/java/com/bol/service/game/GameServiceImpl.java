package com.bol.service.game;

import com.bol.exception.BusinessException;
import com.bol.exception.NotFoundException;
import com.bol.model.entity.BoardEntity;
import com.bol.model.entity.GameEntity;
import com.bol.model.entity.PlayerEntity;
import com.bol.model.enums.DifficultyLevel;
import com.bol.model.enums.GameStatus;
import com.bol.model.enums.KalahaConstants;
import com.bol.repository.GameRepository;
import com.bol.service.board.BoardService;
import com.bol.service.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;


@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository repository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private BoardService boardService;

    @Override
    @Transactional
    public GameEntity createGame(PlayerEntity player, DifficultyLevel difficultyLevel) {

        PlayerEntity firstPlayer = playerService.getById(player.getId());
        BoardEntity board = boardService.createBoard(difficultyLevel);

        GameEntity game = new GameEntity();
        game.setBoard(board);
        game.setFirstPlayer(firstPlayer);
        game.setStatus(GameStatus.NEW);
        game.setDifficultyLevel(difficultyLevel);
        return repository.save(game);
    }

    @Override
    @Transactional
    public GameEntity connect(PlayerEntity player, Long gameId) {
        PlayerEntity secondPlayer = playerService.getById(player.getId());
        GameEntity game = getGameById(gameId);

        if (Objects.nonNull(game.getSecondPlayer())) {
            throw new BusinessException("SECOND.PLAYER.IS.NOT.NULL");
        }
        if (game.getFirstPlayer().getId().equals(secondPlayer.getId())) {
            throw new BusinessException("TWO.PLAYERS.ARE.EQUAL");
        }

        game.setSecondPlayer(secondPlayer);
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setPlayerTurn(selectTurn(game));
        return repository.save(game);
    }


    /**
     * Returns an String object that can show whose turn is.<p>
     *
     * @param game which has to decided on its turn
     * @return the name of player who is turn to play
     */

    private String selectTurn(GameEntity game) {
        Random random = new Random();
        if (random.nextBoolean()) {
            return game.getFirstPlayer().getUserName();
        } else {
            return game.getSecondPlayer().getUserName();
        }
    }

    @Override
    public GameEntity getGameById(Long gameId) {
        Optional<GameEntity> gameEntityOptional = repository.findById(gameId);
        GameEntity game;
        if (gameEntityOptional.isPresent()) {
            game = gameEntityOptional.get();
        } else {
            throw new NotFoundException("GAME.NOT.EXIST");
        }
        return game;
    }

    @Override
    @Transactional
    public GameEntity connectToRandomGame(PlayerEntity player) {
        PlayerEntity secondPlayer = playerService.getById(player.getId());
        GameEntity game = repository.findByStatus(GameStatus.NEW)
                .stream().filter(item -> item.getStatus().equals(GameStatus.NEW))
                .findFirst()
                .orElseThrow(() -> new BusinessException("GAME.NOT.EXIST"));

        if (game.getFirstPlayer().getId().equals(secondPlayer.getId())) {
            throw new BusinessException("TWO.PLAYERS.ARE.EQUAL");
        }
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setSecondPlayer(secondPlayer);
        game.setPlayerTurn(selectTurn(game));
        return repository.save(game);
    }

    @Override
    @Transactional
    public GameEntity gamePlay(Long gameId, Integer pitId) {
        GameEntity game = getGameById(gameId);

        // validate game based on pit id for legal movement
        sowValidation(game, pitId);

        // keep the pit index, used for sowing the stones in right pits
        game.setCurrentPitIndex(pitId);

        // move the stones
        sow(game);

        // we switch the turn if the last sow was not on any of pit houses (left or right)
        if (!game.getCurrentPitIndex().equals(KalahaConstants.rightPitHouseId.getValue())
                && !game.getCurrentPitIndex().equals(KalahaConstants.leftPitHouseId.getValue())) {
            game.setPlayerTurn(nextTurn(game));
        }
        return game;
    }


    private void sowValidation(GameEntity game, Integer pitId) {

        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new BusinessException("Game.IS.FINISHED");
        }

        // No movement on House pits
        if (pitId.equals(KalahaConstants.rightPitHouseId.getValue()) || pitId.equals(KalahaConstants.leftPitHouseId.getValue())) {
            throw new BusinessException("PIT.NUMBER.CHOSEN.WRONGLY");
        }

        // we need to check if request comes from the right player otherwise we do not sow the game. In other words,
        // we keep the turn for the correct player
        if (game.getPlayerTurn().equals(game.getFirstPlayer().getUserName()) && pitId > KalahaConstants.leftPitHouseId.getValue() ||
                game.getPlayerTurn().equals(game.getSecondPlayer().getUserName()) && pitId < KalahaConstants.leftPitHouseId.getValue()) {
            throw new BusinessException("PIT.NUMBER.CHOSEN.WRONGLY");
        }

        int stones = boardService.getStones(game.getBoard(), pitId);

        // No movement for empty Pits
        if (stones == KalahaConstants.emptyStone.getValue()) {
            throw new BusinessException("PIT.NUMBER.CHOSEN.WRONGLY");
        }
    }

    private void sow(GameEntity game) {
        int stones = boardService.getStones(game.getBoard(), game.getCurrentPitIndex());
        boardService.setStones(game.getBoard(), game.getCurrentPitIndex(), KalahaConstants.emptyStone.getValue());

        // simply sow all stones except the last one
        for (int i = 0; i < stones - 1; i++) {
            sowRight(game);
        }

        // simply sow the last stone
        sowLastStone(game);
        checkWinner(game);
    }


    private void sowRight(GameEntity game) {

        int currentPitIndex = game.getCurrentPitIndex() % KalahaConstants.rightPitHouseId.getValue() + 1;

        if ((game.getPlayerTurn().equals(game.getFirstPlayer().getUserName()) && currentPitIndex == KalahaConstants.rightPitHouseId.getValue())
                || game.getPlayerTurn().equals(game.getSecondPlayer().getUserName()) && currentPitIndex == KalahaConstants.leftPitHouseId.getValue()) {
            currentPitIndex = currentPitIndex % KalahaConstants.rightPitHouseId.getValue() + 1;
        }
        game.setCurrentPitIndex(currentPitIndex);
        int targetStones = boardService.getStones(game.getBoard(), currentPitIndex);
        boardService.setStones(game.getBoard(), currentPitIndex, targetStones + 1);
    }

    private void sowLastStone(GameEntity game) {
        int currentPitIndex = game.getCurrentPitIndex() % KalahaConstants.rightPitHouseId.getValue() + 1;

        if ((game.getPlayerTurn().equals(game.getFirstPlayer().getUserName()) && currentPitIndex == KalahaConstants.rightPitHouseId.getValue())
                || game.getPlayerTurn().equals(game.getSecondPlayer().getUserName()) && currentPitIndex == KalahaConstants.leftPitHouseId.getValue()) {
            currentPitIndex = currentPitIndex % KalahaConstants.rightPitHouseId.getValue() + 1;
        }
        game.setCurrentPitIndex(currentPitIndex);
        int targetStones = boardService.getStones(game.getBoard(), currentPitIndex);

       /* we are sowing the last stone and the current player's pit is empty but the opposite pit is not empty, therefore,
        we collect the opposite's Pit stones plus the last stone and add them to the House Pit of current player and
        make the opposite Pit empty*/
        if (game.getPlayerTurn().equals(game.getFirstPlayer().getUserName()) && currentPitIndex < KalahaConstants.leftPitHouseId.getValue()) {
            // It's the last stone and we need to check the opposite player's pit status
            int oppositeStone = boardService.getStones(game.getBoard(), KalahaConstants.rightPitHouseId.getValue() - currentPitIndex);
            if (targetStones == 0 && oppositeStone != 0) {
                boardService.setStones(game.getBoard(), KalahaConstants.rightPitHouseId.getValue() - currentPitIndex, KalahaConstants.emptyStone.getValue());
                boardService.setStones(game.getBoard(), KalahaConstants.leftPitHouseId.getValue(), boardService.getStones(game.getBoard(), KalahaConstants.leftPitHouseId.getValue()) + oppositeStone + 1);
                return;
            }
        } else if (game.getPlayerTurn().equals(game.getSecondPlayer().getUserName()) && currentPitIndex > KalahaConstants.leftPitHouseId.getValue()) {
            int oppositeStone = boardService.getStones(game.getBoard(), KalahaConstants.rightPitHouseId.getValue() - currentPitIndex);
            if (targetStones == 0 && oppositeStone != 0) {
                boardService.setStones(game.getBoard(), KalahaConstants.rightPitHouseId.getValue() - currentPitIndex, KalahaConstants.emptyStone.getValue());
                boardService.setStones(game.getBoard(), KalahaConstants.rightPitHouseId.getValue(), boardService.getStones(game.getBoard(), KalahaConstants.rightPitHouseId.getValue()) + oppositeStone + 1);
                return;
            }
        }
        boardService.setStones(game.getBoard(), currentPitIndex, targetStones + 1);
    }

    private void checkWinner(GameEntity game) {
        BoardEntity board = game.getBoard();
        Integer firstPlayerStones = board.getFirstPitPlayerA() + board.getSecondPitPlayerA() + board.getThirdPitPlayerA()
                + board.getForthPitPlayerA() + board.getFifthPitPlayerA() + board.getSixthPitPlayerA();

        Integer secondPlayerStones = board.getFirstPitPlayerB() + board.getSecondPitPlayerB() + board.getThirdPitPlayerB()
                + board.getForthPitPlayerB() + board.getFifthPitPlayerB() + board.getSixthPitPlayerB();

        if (firstPlayerStones.equals(0)) {
            if (secondPlayerStones + board.getRightPitHouseId() > board.getLeftPitHouseId()) {
                game.setWinner(game.getSecondPlayer().getUserName());
            } else {
                game.setWinner(game.getFirstPlayer().getUserName());
            }
            game.setStatus(GameStatus.FINISHED);
        } else if (secondPlayerStones.equals(0)) {
            if (firstPlayerStones + board.getLeftPitHouseId() > board.getRightPitHouseId()) {
                game.setWinner(game.getFirstPlayer().getUserName());
            } else {
                game.setWinner(game.getSecondPlayer().getUserName());
            }
            game.setStatus(GameStatus.FINISHED);
        }
    }

    /**
     * Change the player turn for specified game<p>
     *
     * @param game which has to be changed its turn
     * @return the string object showing the turn of player
     */
    private String nextTurn(GameEntity game) {
        if (game.getPlayerTurn().equals(game.getFirstPlayer().getUserName()))
            return game.getSecondPlayer().getUserName();
        return game.getFirstPlayer().getUserName();
    }
}
