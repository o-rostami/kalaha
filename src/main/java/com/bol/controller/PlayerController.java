package com.bol.controller;

import com.bol.config.SwaggerConfig;
import com.bol.model.dto.PlayerDto;
import com.bol.model.mapper.PlayerMapper;
import com.bol.service.player.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
@Api(tags = {SwaggerConfig.PLAYER_CONTROLLER_TAG})
public class PlayerController {

    @Autowired
    private PlayerService service;
    @Autowired
    private PlayerMapper mapper;


    @PostMapping
    @ApiOperation(value = "Endpoint for creating the player",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = Long.class,
            httpMethod = "POST")
    public Long createPlayer(
            @RequestBody PlayerDto playerDto) {

        return service.createPlayer(mapper.dtoToEntity(playerDto));
    }


    @GetMapping(value = "{id}")
    @ApiOperation(value = "Endpoint for returning the player",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = PlayerDto.class, httpMethod = "GET")
    public PlayerDto getPlayer(
            @ApiParam(value = "The id of player created by calling createPlayer() method. It can't be empty or null",
                    required = true)
            @PathVariable(value = "id") Long playerId) {
        return mapper.entityToDto(service.getById(playerId));
    }

}
