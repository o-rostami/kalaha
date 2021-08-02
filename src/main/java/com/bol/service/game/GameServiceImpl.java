package com.bol.service.game;

import com.bol.exception.BusinessException;
import com.bol.exception.NotFoundException;
import com.bol.model.entity.BoardEntity;
import com.bol.model.entity.GameEntity;
import com.bol.model.entity.PlayerEntity;
import com.bol.model.enums.DifficultyLevel;
import com.bol.model.enums.GameStatus;
import com.bol.model.enums.MancalaConstants;
import com.bol.repository.GameRepository;
import com.bol.service.board.BoardService;
import com.bol.service.player.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;


@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final PlayerService playerService;
    private final BoardService boardService;


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
        // validate the moving condition based on the pit id
        sowValidation(game, pitId);

        int stones = boardService.getStones(game.getBoard(), pitId);
        boardService.setStones(game.getBoard(), pitId, MancalaConstants.emptyStone.getValue());

        // keep the pit index, used for sowing the stones in right pits
        game.setCurrentPitIndex(pitId);

        // simply sow all stones except the last one
        for (int i = 0; i < stones - 1; i++) {
            sowRight(game, false);
        }

        // simply sow the last stone
        sowRight(game, true);


        // we switch the turn if the last sow was not on any of pit houses (left or right)
        if (!game.getCurrentPitIndex().equals(MancalaConstants.rightPitHouseId.getValue())
                && !game.getCurrentPitIndex().equals(MancalaConstants.leftPitHouseId.getValue())) {
            game.setPlayerTurn(nextTurn(game));
        }

        return game;
    }

    private void sowValidation(GameEntity game, Integer pitId) {

        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new BusinessException("Game.IS.FINISHED");
        }

        // No movement on House pits
        if (pitId.equals(MancalaConstants.rightPitHouseId.getValue()) || pitId.equals(MancalaConstants.leftPitHouseId.getValue())) {
            throw new BusinessException("PIT.NUMBER.CHOSEN.WRONGLY");
        }

        // we need to check if request comes from the right player otherwise we do not sow the game. In other words,
        // we keep the turn for the correct player
        if (game.getPlayerTurn().equals(game.getFirstPlayer().getUserName()) && pitId > MancalaConstants.leftPitHouseId.getValue() ||
                game.getPlayerTurn().equals(game.getSecondPlayer().getUserName()) && pitId < MancalaConstants.leftPitHouseId.getValue()) {
            throw new BusinessException("PIT.NUMBER.CHOSEN.WRONGLY");
        }

        int stones = boardService.getStones(game.getBoard(), pitId);

        // No movement for empty Pits
        if (stones == MancalaConstants.emptyStone.getValue()) {
            throw new BusinessException("PIT.NUMBER.CHOSEN.WRONGLY");
        }
    }

    //     sow the game one pit to the right
    private void sowRight(GameEntity game, Boolean lastStone) {

        int currentPitIndex = game.getCurrentPitIndex() % MancalaConstants.rightPitHouseId.getValue() + 1;

        String playerTurn = game.getPlayerTurn();

        if ((currentPitIndex == MancalaConstants.rightPitHouseId.getValue() && playerTurn.equals(game.getSecondPlayer().getUserName())) ||
                (currentPitIndex == MancalaConstants.leftPitHouseId.getValue() && playerTurn.equals(game.getFirstPlayer().getUserName()))) {
            currentPitIndex = currentPitIndex % MancalaConstants.rightPitHouseId.getValue() + 1;
        }


        game.setCurrentPitIndex(currentPitIndex);


        int targetStones = boardService.getStones(game.getBoard(), currentPitIndex);
        if (!lastStone || currentPitIndex == MancalaConstants.rightPitHouseId.getValue() || currentPitIndex == MancalaConstants.rightPitHouseId.getValue()) {
            boardService.setStones(game.getBoard(), currentPitIndex, targetStones + 1);
            return;
        }

        // It's the last stone and we need to check the opposite player's pit status
//        KalahaPit oppositePit = game.getPit(MancalaConstants.rightPitHouseId.getValue() - currentPitIndex);
        int oppositeStones = boardService.getStones(game.getBoard(), MancalaConstants.rightPitHouseId.getValue() - currentPitIndex);

        // we are sowing the last stone and the current player's pit is empty but the opposite pit is not empty, therefore,
        // we collect the opposite's Pit stones plus the last stone and add them to the House Pit of current player and
        // make the opposite Pit empty
        if (targetStones == 0 && oppositeStones != 0) {
            boardService.setStones(game.getBoard(), MancalaConstants.rightPitHouseId.getValue() - currentPitIndex, 0);

            int pitHouseIndex = currentPitIndex < MancalaConstants.leftPitHouseId.getValue() ? MancalaConstants.leftPitHouseId.getValue() : MancalaConstants.rightPitHouseId.getValue();
            int pitHouseStones = boardService.getStones(game.getBoard(), pitHouseIndex);
            boardService.setStones(game.getBoard(), pitHouseIndex, pitHouseStones + oppositeStones + 1);
            return;
        }
        boardService.setStones(game.getBoard(), currentPitIndex, targetStones + 1);
    }

    private String nextTurn(GameEntity game) {
        if (game.getPlayerTurn().equals(game.getFirstPlayer().getUserName()))
            return game.getSecondPlayer().getUserName();
        return game.getFirstPlayer().getUserName();
    }
}
