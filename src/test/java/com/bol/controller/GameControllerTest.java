package com.bol.controller;

import com.bol.model.dto.GamePlayDto;
import com.bol.model.entity.BoardEntity;
import com.bol.model.entity.GameEntity;
import com.bol.model.entity.PlayerEntity;
import com.bol.model.enums.DifficultyLevel;
import com.bol.model.enums.GameStatus;
import com.bol.service.game.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
 class GameControllerTest {

    private static PlayerEntity firstPlayer;
    private static PlayerEntity secondPlayer;
    private static GameEntity gameEntity;
    private static BoardEntity boardEntity;
    private static GamePlayDto gamePlayDto;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private GameService gameService;


    @BeforeAll
    public static void init() {
        firstPlayer = new PlayerEntity();
        firstPlayer.setUserName("test1");
        firstPlayer.setId(1L);

        secondPlayer = new PlayerEntity();
        secondPlayer.setUserName("test2");
        secondPlayer.setId(2L);

        boardEntity = new BoardEntity();
        boardEntity.setId(1L);
        boardEntity.setFirstPitPlayerA(DifficultyLevel.EASY.getValue());
        boardEntity.setSecondPitPlayerA(DifficultyLevel.EASY.getValue());
        boardEntity.setThirdPitPlayerA(DifficultyLevel.EASY.getValue());
        boardEntity.setForthPitPlayerA(DifficultyLevel.EASY.getValue());
        boardEntity.setFifthPitPlayerA(DifficultyLevel.EASY.getValue());
        boardEntity.setSixthPitPlayerA(DifficultyLevel.EASY.getValue());

        boardEntity.setFirstPitPlayerB(DifficultyLevel.EASY.getValue());
        boardEntity.setSecondPitPlayerB(DifficultyLevel.EASY.getValue());
        boardEntity.setThirdPitPlayerB(DifficultyLevel.EASY.getValue());
        boardEntity.setForthPitPlayerB(DifficultyLevel.EASY.getValue());
        boardEntity.setFifthPitPlayerB(DifficultyLevel.EASY.getValue());
        boardEntity.setSixthPitPlayerB(DifficultyLevel.EASY.getValue());


        gameEntity = new GameEntity();
        gameEntity.setFirstPlayer(firstPlayer);
        gameEntity.setSecondPlayer(secondPlayer);
        gameEntity.setDifficultyLevel(DifficultyLevel.EASY);

        gameEntity.setBoard(boardEntity);
        gameEntity.setId(1L);

        gamePlayDto = new GamePlayDto();
        gamePlayDto.setPitId(1);
        gamePlayDto.setId(1L);
    }


    @Test
    @Order(1)
     void createRecord_success() throws Exception {
        gameEntity.setStatus(GameStatus.NEW);
        Mockito.when(gameService.createGame(firstPlayer, DifficultyLevel.EASY)).thenReturn(gameEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/game/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(gameEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.status", is(GameStatus.NEW.name())))
                .andExpect(jsonPath("$.firstPlayer.userName", is(firstPlayer.getUserName())))
                .andExpect(jsonPath("$.board.firstPitPlayerA", is(boardEntity.getFirstPitPlayerA())));
    }

    @Test
    @Order(2)
     void connectGame_success() throws Exception {
        gameEntity.setStatus(GameStatus.IN_PROGRESS);
        Mockito.when(gameService.connect(secondPlayer, 1L)).thenReturn(gameEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/game/connect")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(gameEntity));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.status", is(GameStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.secondPlayer.userName", is(secondPlayer.getUserName())));
    }

    @Test
    @Order(3)
     void connectToRandomGame_success() throws Exception {
        gameEntity.setStatus(GameStatus.IN_PROGRESS);
        Mockito.when(gameService.connectToRandomGame(secondPlayer)).thenReturn(gameEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/game/connect/random")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(secondPlayer));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.status", is(GameStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.secondPlayer.userName", is(secondPlayer.getUserName())));
    }


    @Test
    @Order(4)
     void gamePlay_success() throws Exception {
        gameEntity.setStatus(GameStatus.IN_PROGRESS);
        gameEntity.getBoard().setFirstPitPlayerA(0);
        Mockito.when(gameService.gamePlay(1L, 1)).thenReturn(gameEntity);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/game/gameplay")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(gamePlayDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.status", is(GameStatus.IN_PROGRESS.name())))
                .andExpect(jsonPath("$.firstPlayer.userName", is(firstPlayer.getUserName())))
                .andExpect(jsonPath("$.secondPlayer.userName", is(secondPlayer.getUserName())))
                .andExpect(jsonPath("$.board.firstPitPlayerA", is(0)));
    }

}
