package com.bol.controller;

import com.bol.model.dto.PlayerDto;
import com.bol.model.mapper.PlayerMapper;
import com.bol.service.player.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
@Api(value = "Player game API. Set of endpoints for Creating and Updating the Players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService service;
    private final PlayerMapper mapper;


    @PostMapping
    @ApiOperation(value = "Endpoint for creating the player",
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = Long.class,
            httpMethod = "POST")
    public Long createPlayer(
            @RequestBody PlayerDto playerDto) {
        return service.createPlayer(playerDto);
    }

    @PutMapping
    @ApiOperation(value = "Endpoint for updating the player",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "PUT")
    public void updatePlayer(
            @RequestBody PlayerDto playerDto) {
        service.updatePlayer(playerDto);
    }


    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "Endpoint for deleting the player",
            produces = MediaType.APPLICATION_JSON_VALUE,
            httpMethod = "DELETE")
    public void deletePlayer(
            @ApiParam(value = "The id of player created by calling createPlayer() method. It can't be empty or null",
                    required = true)
            @PathVariable(value = "id") Long playerId) {
        service.deletePlayer(playerId);
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