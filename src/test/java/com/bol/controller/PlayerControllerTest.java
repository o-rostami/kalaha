package com.bol.controller;

import com.bol.model.entity.PlayerEntity;
import com.bol.service.player.PlayerService;
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
public class PlayerControllerTest {

    private static PlayerEntity RECORD;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PlayerService playerService;


    @BeforeAll
    public static void init() {
        RECORD = new PlayerEntity();
        RECORD.setUserName("test1");
        RECORD.setId(1L);
    }

    @Test
    @Order(1)
    public void getPlayerById_success() throws Exception {
        Mockito.when(playerService.getById(RECORD.getId())).thenReturn((RECORD));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/player/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userName", is("test1")));
    }

    @Test
    @Order(2)
    public void createRecord_success() throws Exception {
        Mockito.when(playerService.createPlayer(RECORD)).thenReturn(RECORD.getId());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(RECORD));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is(1)));
    }

}
