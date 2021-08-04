package com.bol.controller;

import com.bol.config.SwaggerConfig;
import com.bol.exception.NotNullException;
import com.bol.model.dto.*;
import com.bol.model.entity.GameEntity;
import com.bol.model.mapper.GameMapper;
import com.bol.model.mapper.PlayerMapper;
import com.bol.service.game.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/game")
@Api(tags = {SwaggerConfig.GAME_CONTROLLER_TAG})
public class GameController {

    @Autowired
    private GameService service;
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/create")
    @ApiOperation(value = "Endpoint for creating the game",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GameDto.class,
            httpMethod = "POST")
    public GameDto createGame(@RequestBody GameCreateDto gameCreateDto) {
        if (Objects.isNull(gameCreateDto) ||
                Objects.isNull(gameCreateDto.getFirstPlayer()) ||
                Objects.isNull(gameCreateDto.getFirstPlayer().getId()))
            throw new NotNullException("FIRST.PLAYER.ID.IS.NULL", "FirstPlayer");
        if (Objects.isNull(gameCreateDto.getDifficultyLevel()))
            throw new NotNullException("DIFFICULTY.LEVEL.IS.NULL", "DifficultyLevel");

        return gameMapper.entityToDto(service.createGame(
                playerMapper.dtoToEntity(gameCreateDto.getFirstPlayer()),
                gameCreateDto.getDifficultyLevel()
        ));
    }

    @PostMapping("/connect")
    @ApiOperation(value = "Endpoint for connecting to the game",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GameDto.class,
            httpMethod = "POST")
    public GameDto connect(@RequestBody GameConnectDto gameConnectDto) {
        if (Objects.isNull(gameConnectDto.getId()))
            throw new NotNullException("GAME.ID.IS.NULL", "GameId");
        if (Objects.isNull(gameConnectDto) ||
                Objects.isNull(gameConnectDto.getSecondPlayer()) ||
                Objects.isNull(gameConnectDto.getSecondPlayer().getId()))
            throw new NotNullException("SECOND.PLAYER.ID.IS.NULL", "SecondPlayer");
        GameEntity game = service.connect(
                playerMapper.dtoToEntity(gameConnectDto.getSecondPlayer()),
                gameConnectDto.getId());
        GameDto gameDto = gameMapper.entityToDto(game);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getId(), gameDto);
        return gameDto;
    }

    @PostMapping("/connect/random")
    @ApiOperation(value = "Endpoint for connecting to a random game",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GameDto.class,
            httpMethod = "POST")
    public GameDto connectToRandomGame(@RequestBody PlayerDto player) {
        GameEntity game = service.connectToRandomGame(playerMapper.dtoToEntity(player));
        GameDto gameDto = gameMapper.entityToDto(game);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getId(), gameDto);
        return gameDto;
    }

    @PostMapping("/gameplay")
    @ApiOperation(value = "Endpoint for sowing the stones",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = GameDto.class,
            httpMethod = "POST")
    public GameDto gamePlay(@RequestBody GamePlayDto request) {
        if (Objects.isNull(request.getId()))
            throw new NotNullException("GAME.ID.IS.NULL", "GameId");
        if (Objects.isNull(request.getPitId()))
            throw new NotNullException("PIT.ID.IS.NULL", "PitId");
        GameEntity game = service.gamePlay(request.getId(), request.getPitId());
        GameDto gameDto = gameMapper.entityToDto(game);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getId(), gameDto);
        return gameDto;
    }


}
