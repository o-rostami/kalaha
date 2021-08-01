package com.bol.service.game;


import com.bol.model.entity.GameEntity;
import com.bol.model.entity.PlayerEntity;
import com.bol.model.enums.DifficultyLevel;

public interface GameService {

    GameEntity createGame(PlayerEntity player, DifficultyLevel difficultyLevel);

    GameEntity connect(PlayerEntity player, Long gameId);

    GameEntity connectToRandomGame(PlayerEntity player);

    GameEntity getGameById(Long gameId);

    GameEntity gamePlay(Long gameId,Integer pitId);



}
